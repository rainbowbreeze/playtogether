package it.rainbowbreeze.playtog.logic;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;
import java.util.List;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.domain.Player;
import it.rainbowbreeze.playtog.game.Game;
import it.rainbowbreeze.playtog.game.model.GameResult;
import it.rainbowbreeze.playtog.registration.Registration;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class BackendHelper {
    private static final String LOG_TAG = BackendHelper.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final AppPrefsManager mAppPrefsManager;
    private Registration mRegService = null;
    private Game mGameService = null;

    protected final Context mAppContext;
    private static final String SENDER_ID = "681581883585";  // Project ID of the backend app

    public BackendHelper(Context appContext, ILogFacility logFacility, AppPrefsManager appPrefsManager) {
        mAppContext = appContext;
        mLogFacility = logFacility;
        mAppPrefsManager = appPrefsManager;
    }

    public void registerClient() {
        mLogFacility.v(LOG_TAG, "Registering this client to the GCM backend");
        setupRegistration();

        try {
            String regId = mAppPrefsManager.getGCMRegId();
            if (TextUtils.isEmpty(regId)) {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(mAppContext);
                regId = gcm.register(SENDER_ID);
                mLogFacility.v(LOG_TAG, "Device registered, registration ID: " + regId);
                mAppPrefsManager.setGCMRegId(regId);
            } else {
                mLogFacility.v(LOG_TAG, "Using existing registration ID: " + regId);
            }

            // You should send the registration ID to your server over HTTP,
            // so it can use GCM/HTTP or CCS to send messages to your app.
            // The request to your server should be authenticated if your app
            // is using accounts.
            Player currentPlayer = mAppPrefsManager.getCurrentPlayer();
            mRegService.register(regId, currentPlayer.getSocialId()).execute();
            mLogFacility.v(LOG_TAG, "Registered the client to GCM backend");

        } catch (IOException ex) {
            mLogFacility.e(LOG_TAG, "Error registering the client", ex);
        }
    }

    public void unregisterClient() {
        mLogFacility.v(LOG_TAG, "Unregistering this client from the GCM backend");
        setupRegistration();

        String regId = mAppPrefsManager.getGCMRegId();
        if (TextUtils.isEmpty(regId)) {
            mLogFacility.e(LOG_TAG, "Cannot unregister a client that doesn't have a registered ID");
            return;
        }
        try {
            // Don't need to unregister from GCM server, only from the backend.
            // See Look at GoogleCloudMessaging.html#unregister() reference for detailed instructions
            mRegService.unregister(regId).execute();
            mLogFacility.v(LOG_TAG, "Unregistered this client from the GCM backend");
        } catch (IOException ex) {
            mLogFacility.e(LOG_TAG, "Error unregistering the client", ex);
        }
    }

    public boolean searchForPlayers() {
        mLogFacility.v(LOG_TAG, "Searching for new players");
        setupGame();

        Player player = mAppPrefsManager.getCurrentPlayer();
        try {
            GameResult result = mGameService.searchForPlayers(player.getSocialId(), "IT-MIL-CON-4-FOOSBALL").execute();
            if (result.getSuccess()) {
                mLogFacility.v(LOG_TAG, "Asked players for a new game");
                mAppPrefsManager.setCurrentGameId(result.getGameId());
                return true;
            } else {
                mLogFacility.v(LOG_TAG, "An error happened while asking players to join a new game");
            }
        } catch (IOException ex) {
            mLogFacility.e(LOG_TAG, "Error searching for new players", ex);
        }
        return false;
    }

    public boolean startGame(String gameId, List<String> playerIds) {
        mLogFacility.v(LOG_TAG, "Starting a game with selected players " + gameId);
        setupGame();

        try {
            GameResult result = mGameService.start(gameId, playerIds).execute();
            if (result.getSuccess()) {
                mLogFacility.v(LOG_TAG, "Started a new game, good luck!");
                return true;
            } else {
                mLogFacility.v(LOG_TAG, "An error happened starting a new game");
            }
        } catch (IOException ex) {
            mLogFacility.e(LOG_TAG, "Error starting a new game", ex);
        }
        return false;
    }

    public boolean participateToAGame(String gameId) {
        mLogFacility.v(LOG_TAG, "Participating to a game " + gameId);
        setupGame();

        Player player = mAppPrefsManager.getCurrentPlayer();
        try {
            GameResult result = mGameService.participate(gameId, player.getSocialId()).execute();
            if (result.getSuccess()) {
                mLogFacility.v(LOG_TAG, "Asked to participate to a game, now waiting for the approval");
                return true;
            } else {
                mLogFacility.v(LOG_TAG, "An error happened participating to a game");
            }
        } catch (IOException ex) {
            mLogFacility.e(LOG_TAG, "Error participating to a game", ex);
        }
        return false;
    }

    /**
     * Setting up the registration object for communicating with the backend server
     */
    private void setupRegistration() {
        if (mRegService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            //Run in the emulator, connect to local server
            if (Build.FINGERPRINT.startsWith("generic")) {
                // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                // otherwise they can be skipped
                builder
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                    throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
            // Run on device, connect on real server
            } else {
                builder.setRootUrl("https://play-together-2015.appspot.com/_ah/api/");
            }
            mRegService = builder.build();
        }
    }

    /**
     * Setting up the registration object for communicating with the backend server
     */
    private void setupGame() {
        if (mGameService == null) {
            Game.Builder builder = new Game.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null);

            //Run in the emulator, connect to local server
            if (Build.FINGERPRINT.startsWith("generic")) {
                // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                // otherwise they can be skipped
                builder
                        .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                        .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                            @Override
                            public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                    throws IOException {
                                abstractGoogleClientRequest.setDisableGZipContent(true);
                            }
                        });
                // Run on device, connect on real server
            } else {
                builder.setRootUrl("https://play-together-2015.appspot.com/_ah/api/");
            }
            mGameService = builder.build();
        }
    }

}
