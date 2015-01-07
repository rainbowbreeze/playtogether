package it.rainbowbreeze.playtog.logic;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.domain.Player;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class GcmIntentService extends IntentService {
    private static final String LOG_TAG = GcmIntentService.class.getSimpleName();

    // Needs to match with the corresponding GCM sender class in the backend
    private static final String EXTRA_MESSAGE = "message";
    private static final String EXTRA_GCMACTION_TYPE = "gcmActionType";
    private static final String EXTRA_PLAYER_GPLUS_ID = "playerId";
    // Needs to match with the corresponding class in the backend
    private static final String GCMACTION_SEARCH_FOR_PLAYERS = "SearchForPlayers";
    private static final String GCMACTION_ACCEPTED = "Accepted";
    private static final String GCMACTION_DENIED = "Denied";
    private static final String GCMACTION_NEW_USER_FOR_GAME = "NewUserForGame";

    @Inject ILogFacility mLogFacility;
    @Inject AppPrefsManager mAppPrefsManager;

    public GcmIntentService() {
        super(LOG_TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp)getApplicationContext()).inject(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mLogFacility.v(LOG_TAG, "Received GCM message");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (extras != null && !extras.isEmpty()) {  // has effect of unparcelling Bundle
            // Since we're not using two way messaging, this is all we really to check for
            if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                mLogFacility.v(LOG_TAG, "Message received: " + extras.toString());
                showToast(extras.getString(EXTRA_MESSAGE));

                String matchType = extras.getString(EXTRA_GCMACTION_TYPE);
                String gplusId = extras.getString(EXTRA_PLAYER_GPLUS_ID);
                if (GCMACTION_SEARCH_FOR_PLAYERS.equalsIgnoreCase(matchType)) {

                    Player player = mAppPrefsManager.getCurrentPlayer();
                    if (gplusId.equals(player.getSocialId())) {
                        mLogFacility.v(LOG_TAG, "Request to start a new game from the same player, aborting");
                    } else {
                        mLogFacility.v(LOG_TAG, gplusId + " launched a search for players");
                        Intent intent2 = new Intent(getApplicationContext(), GPlusCommunicationService.class);
                        intent2.setAction(GPlusCommunicationService.ACTION_SEARCH_FOR_PLAYERS);
                        intent2.putExtra(GPlusCommunicationService.EXTRA_USER_ID, gplusId);
                        startService(intent2);
                    }

                } else if (GCMACTION_ACCEPTED.equalsIgnoreCase(matchType)) {
                    mLogFacility.v(LOG_TAG, "Current player has been accepted for a game se has asked to participate");

                } else if (GCMACTION_DENIED.equalsIgnoreCase(matchType)) {
                    mLogFacility.v(LOG_TAG, "Current player has been rejected for a game se has asked to participate");

                } else if (GCMACTION_NEW_USER_FOR_GAME.equalsIgnoreCase(matchType)) {
                    mLogFacility.v(LOG_TAG, "A new player want to participate to a game opened by the current user");
                    if (!TextUtils.isEmpty(gplusId)) {
                        Intent intent2 = new Intent(getApplicationContext(), GPlusCommunicationService.class);
                        intent2.setAction(GPlusCommunicationService.ACTION_ADD_USER_TO_GAME);
                        intent2.putExtra(GPlusCommunicationService.EXTRA_USER_ID, gplusId);
                        startService(intent2);
                    } else {
                        mLogFacility.e(LOG_TAG, "Cannot add a player that hasn't a gplus valid id, sorry :(");
                    }
                }

            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    protected void showToast(final String message) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }
}
