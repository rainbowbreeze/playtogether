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
import it.rainbowbreeze.playtog.ui.JoinGameActivity;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class GPlusCommunicationService extends GoogleApiClientBaseService {
    private static final String LOG_TAG = GPlusCommunicationService.class.getSimpleName();

    public static final String ACTION_SEARCH_FOR_PLAYERS = "it.rainbowbreeze.playtog.Action.Plus.SearchForPlayers";
    public static final String ACTION_LOAD_CURRENT_USER = "it.rainbowbreeze.playtog.Action.Plus.LoadCurrentUser";
    public static final String ACTION_ADD_USER_TO_GAME = "it.rainbowbreeze.playtog.Action.Plus.AddUserToGame";
    public static final String EXTRA_USER_ID = "Param.UserId";

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
        return new String[] {ACTION_SEARCH_FOR_PLAYERS, ACTION_LOAD_CURRENT_USER, ACTION_ADD_USER_TO_GAME};
    }

    @Override
    protected GoogleApiClient.Builder createGoogleApiClient() {
        return super.createGoogleApiClient().addScope(Plus.SCOPE_PLUS_PROFILE);
    }

    @Override
    public void doYourStuff(Intent intent) {
        String userId = intent.getStringExtra(EXTRA_USER_ID);
        if (TextUtils.isEmpty(userId)) {
            mLogFacility.i(LOG_TAG, "Cannot find user id in the intent, aborting...");
            return;
        }

        if (ACTION_LOAD_CURRENT_USER.equals(intent.getAction())) {
            mLogFacility.v(LOG_TAG, "Loading current user info");
            // Retrieve some profile information to personalize our app for the user.
            Player player = mGPlusHelper.getLogged(mGoogleApiClient);
            if (null != player) {
                mAppPrefsManager.setCurrentPlayer(player);
                mLogFacility.v(LOG_TAG, "Logged user stored as a player");
            } else {
                mLogFacility.v(LOG_TAG, "Cannot store logged G+ user as a player");
            }

        } else if (ACTION_ADD_USER_TO_GAME.equals(intent.getAction())) {
            Player player = mGPlusHelper.get(mGoogleApiClient, userId);
            if (null != player) {
                mPlayerDao.insert(player);
                mLogFacility.v(LOG_TAG, "Added to database G+ person " + player.getName());
            } else {
                mLogFacility.v(LOG_TAG, "Cannot add any G+ person to db because she wasn't found");
            }

        } else if (ACTION_SEARCH_FOR_PLAYERS.equals(intent.getAction())) {
            // Retrieves asking user details
            Player askingPlayer = mGPlusHelper.get(mGoogleApiClient, userId);
            if (null != askingPlayer) {
                mLogFacility.v(LOG_TAG, askingPlayer.getName() + " has asked to join a new game");
                Intent startIntent = new Intent(getApplicationContext(), JoinGameActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startIntent.putExtra(JoinGameActivity.EXTRA_PLAYER_NAME, askingPlayer.getName());
                startIntent.putExtra(JoinGameActivity.EXTRA_PLAYER_PICTURE_URL, askingPlayer.getPictureUrl());
                startIntent.putExtra(JoinGameActivity.EXTRA_GAME_ID, "null");
                startActivity(startIntent);
            } else {
                mLogFacility.i(LOG_TAG, "A request for players has arrived, but is impossible to find the asking player");
            }
        }
    }
}
