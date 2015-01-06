package it.rainbowbreeze.playtog.logic;

import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

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

    public static final String ACTION_LOADCURRENTUSER = "Action.Plus.LoadCurrentUser";
    public static final String ACTION_LOADUSER = "Action.Plus.LoadUser";
    public static final String EXTRA_USERID = "Param.UserId";

    @Inject ILogFacility mLogFacility;
    @Inject PlayerDao mPlayerDao;
    @Inject AppPrefsManager mAppPrefsManager;

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
            Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
            mAppPrefsManager.setCurrentPlayer(Player.createFrom(currentUser));
            mLogFacility.v(LOG_TAG, "Logged user stored as a player");

        } else if (ACTION_LOADUSER.equals(intent.getAction())) {
            mLogFacility.v(LOG_TAG, "Adding to database person id " + userId);
            // Retrieve some profile information to personalize our app for the user.
            People.LoadPeopleResult peopleResult = Plus.PeopleApi.load(mGoogleApiClient, userId).await();
            PersonBuffer personBuffer = peopleResult.getPersonBuffer();
            Person person = null;
            if (personBuffer.getCount() > 0) {
                person = personBuffer.get(0);
                if (null != person) {
                    // Adds the person to the provider
                    Player player = Player.createFrom(person);
                    mPlayerDao.insert(player);
                    mLogFacility.v(LOG_TAG, "Added to database G+ person " + player.getName());
                }
            }
            if (null == person) {
                mLogFacility.v(LOG_TAG, "Cannot find any public G+ person for id " + userId);
            }
            personBuffer.close();

        }


    }
}
