package it.rainbowbreeze.playtog.data;

import android.content.Context;

import java.util.Date;

import it.rainbowbreeze.libs.data.RainbowAppPrefsManager;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.domain.Player;

/**
 *
 * Remember: Key names have to be equal to the ones in the xml file, otherwise two different
 *  settings are managed
 *
 * Created by alfredomorresi on 05/12/14.
 */
public class AppPrefsManager extends RainbowAppPrefsManager {
    private static final String LOG_TAG = AppPrefsManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    public static final String PREFS_FILE_NAME = "PlayTogetherPrefs";
    private static final String NULL_STRING = "null";

    public AppPrefsManager(Context appContext, ILogFacility logFacility) {
        super(appContext, PREFS_FILE_NAME, 0, logFacility);
        mLogFacility = logFacility;
    }

    @Override
    protected void setDefaultValuesInternal() {
        mLogFacility.v(LOG_TAG, "Setting default values of preferences");
        resetCurrentPlayer();
    }

    private static final String PREF_GPLUS_LOGIN_DONE = "pref_gpluslogindone";
    public boolean isGPlusLoginDone() {
        return mAppPreferences.getBoolean(PREF_GPLUS_LOGIN_DONE, false);
    }

    private static final String PREF_GPLUS_BACKENDID = "pref_gplusBackendId";
    private static final String PREF_GPLUS_NAME = "pref_gplusName";
    private static final String PREF_GPLUS_PICTUREURL = "pref_gplusPictureUrl";
    private static final String PREF_GPLUS_SOCIALID = "pref_gplusSocialId";

    /**
     * Creates a player using current logged user data
     * @return
     */
    public Player getCurrentPlayer() {
        Player player = new Player()
                .setAcceptedDate(new Date())
                .setBackendId(mAppPreferences.getString(PREF_GPLUS_BACKENDID, NULL_STRING))
                .setName(mAppPreferences.getString(PREF_GPLUS_NAME, NULL_STRING))
                .setPictureUrl(mAppPreferences.getString(PREF_GPLUS_PICTUREURL, NULL_STRING))
                .setSelected(true)
                .setSocialId(mAppPreferences.getString(PREF_GPLUS_SOCIALID, NULL_STRING));
        return player;
    }

    /**
     * Save current user data in the format of a player
     * @param player
     * @return
     */
    public AppPrefsManager setCurrentPlayer(Player player) {
        mLogFacility.v(LOG_TAG, "Setting current user as new person called " + player.getName());
        openSharedEditor();
        mSharedEditor.putString(PREF_GPLUS_BACKENDID, player.getBackendId());
        mSharedEditor.putString(PREF_GPLUS_NAME, player.getName());
        mSharedEditor.putString(PREF_GPLUS_PICTUREURL, player.getPictureUrl());
        mSharedEditor.putString(PREF_GPLUS_SOCIALID, player.getSocialId());
        mSharedEditor.putBoolean(PREF_GPLUS_LOGIN_DONE, true);
        saveIfNeeded();
        return this;
    }

    /**
     * Reset all the information related to current logged user, and player
     * @return
     */
    public AppPrefsManager resetCurrentPlayer() {
        mLogFacility.v(LOG_TAG, "Resetting current user");
        openSharedEditor();
        mSharedEditor.putString(PREF_GPLUS_BACKENDID, NULL_STRING);
        mSharedEditor.putString(PREF_GPLUS_NAME, NULL_STRING);
        mSharedEditor.putString(PREF_GPLUS_PICTUREURL, NULL_STRING);
        mSharedEditor.putString(PREF_GPLUS_SOCIALID, NULL_STRING);
        mSharedEditor.putBoolean(PREF_GPLUS_LOGIN_DONE, false);
        saveIfNeeded();
        return this;
    }


    /**
     * Google Cloud Message registration id. It should be generated once for each installed app,
     * so even if the user unregisters from the backend, the next time she registers the previous
     * registration id should be user.
     *
     * Look at GoogleCloudMessaging.html#unregister() reference for detailed instructions
     */
    private static final String PREF_GCM_REGID = "pref_gcmRegId";
    public String getGCMRegId() {
        return mAppPreferences.getString(PREF_GCM_REGID, "");
    }
    public AppPrefsManager setGCMRegId(String newValue) {
        openSharedEditor();
        mSharedEditor.putString(PREF_GCM_REGID, newValue);
        saveIfNeeded();
        return this;
    }
}
