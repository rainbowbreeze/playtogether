package it.rainbowbreeze.playtog.data;

import android.content.Context;

import it.rainbowbreeze.libs.data.RainbowAppPrefsManager;
import it.rainbowbreeze.playtog.common.ILogFacility;

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
        setGPlusLoginDone(false);
    }

    // Look at the xml file!
    private static final String PREF_GPLUS_LOGIN_DONE = "pref_gpluslogindone";
    public boolean isGPlusLoginDone() {
        return mAppPreferences.getBoolean(PREF_GPLUS_LOGIN_DONE, false);
    }
    public AppPrefsManager setGPlusLoginDone(boolean newValue) {
        openSharedEditor();
        mSharedEditor.putBoolean(PREF_GPLUS_LOGIN_DONE, newValue);
        saveIfNeeded();
        return this;
    }
}
