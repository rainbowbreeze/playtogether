package it.rainbowbreeze.playtog.logic;

import android.content.Context;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.registration.Registration;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class BackendManager {
    private static final String LOG_TAG = BackendManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final AppPrefsManager mAppPrefsManager;
    private Registration mRegService = null;
    protected final Context mAppContext;
    private static final String SENDER_ID = "681581883585";  // Project ID of the backend app

    public BackendManager(Context appContext, ILogFacility logFacility, AppPrefsManager appPrefsManager) {
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
            mRegService.register(regId).execute();
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


    /**
     * Setting up the registration object for communicating with the backend server
     */
    private void setupRegistration() {
        if (mRegService == null) {
            Registration.Builder builder = new Registration.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // Need setRootUrl and setGoogleClientRequestInitializer only for local testing,
                    // otherwise they can be skipped
                    .setRootUrl("http://10.0.2.2:8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest)
                                throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            mRegService = builder.build();
        }
    }
}
