package it.rainbowbreeze.playtog.logic;

import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.data.PlayerDao;
import it.rainbowbreeze.playtog.domain.Player;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class GPlusCommunicationService extends GoogleApiClientBaseService {
    private static final String LOG_TAG = GPlusCommunicationService.class.getSimpleName();

    public static final String ACTION_LOADCURRENTUSER = "it.rainbowbreeze.playtog.Action.Plus.LoadCurrentUser";
    public static final String ACTION_LOADUSER = "it.rainbowbreeze.playtog.Action.Plus.LoadUser";
    public static final String EXTRA_USERID = "Param.UserId";

    @Inject ILogFacility mLogFacility;
    @Inject PlayerDao mPlayerDao;
    @Inject AppPrefsManager mAppPrefsManager;
    @Inject GPlusHelper mGPlusHelper;

    public GPlusCommunicationService() {
        super(LOG_TAG, LOG_TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplication()).inject(this);
    }

    @Override
    protected Api getApi() {
        return Plus.API;
    }

    @Override
    protected ILogFacility getLogFacility() {
        return mLogFacility;
    }

    @Override
    protected String[] getValidIntentActions() {
        return new String[] {ACTION_LOADCURRENTUSER, ACTION_LOADUSER};
    }

    @Override
    protected GoogleApiClient.Builder createGoogleApiClient() {
        return super.createGoogleApiClient().addScope(Plus.SCOPE_PLUS_PROFILE);
    }

    @Override
    public void doYourStuff(Intent intent) {
        String userId = intent.getStringExtra(EXTRA_USERID);
        if (TextUtils.isEmpty(userId)) {
            mLogFacility.e(LOG_TAG, "Cannot find user id in the intent, aborting...");
            return;
        }

        if (ACTION_LOADCURRENTUSER.equals(intent.getAction())) {
            mLogFacility.v(LOG_TAG, "Loading current user info");
            // Retrieve some profile information to personalize our app for the user.
            Player player = mGPlusHelper.getLogged(mGoogleApiClient);
            if (null != player) {
                mAppPrefsManager.setCurrentPlayer(player);
                mLogFacility.v(LOG_TAG, "Logged user stored as a player");
            } else {
                mLogFacility.v(LOG_TAG, "Cannot store logged G+ user as a player");
            }

        } else if (ACTION_LOADUSER.equals(intent.getAction())) {
            Player player = mGPlusHelper.get(mGoogleApiClient, userId);
            if (null != player) {
                mPlayerDao.insert(player);
                mLogFacility.v(LOG_TAG, "Added to database G+ person " + player.getName());
            } else {
                mLogFacility.v(LOG_TAG, "Cannot add any G+ person to db because she wasn't found");
            }
        }
    }
}
