package it.rainbowbreeze.playtog.logic;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.android.gms.plus.model.people.PersonBuffer;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.domain.Player;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class GPlusHelper {
    private static final String LOG_TAG = GPlusHelper.class.getSimpleName();

    private final ILogFacility mLogFacility;

    public GPlusHelper(ILogFacility logFacility) {
        mLogFacility = logFacility;
    }

    /**
     * Retrieve a {@link it.rainbowbreeze.playtog.domain.Player} from a Google+ user
     *
     * @param googleApiClient
     * @param userId
     * @return
     */
    public Player get(GoogleApiClient googleApiClient, String userId) {
        mLogFacility.v(LOG_TAG, "Retrieving G+ user for id " + userId);
        People.LoadPeopleResult peopleResult = Plus.PeopleApi.load(googleApiClient, userId).await();
        PersonBuffer personBuffer = peopleResult.getPersonBuffer();
        Person person = null;
        Player player = null;
        if (personBuffer.getCount() > 0) {
            person = personBuffer.get(0);
            if (null != person) {
                // Adds the person to the provider
                player = Player.createFrom(person);
            }
        }
        personBuffer.close();
        if (null == person) {
            mLogFacility.v(LOG_TAG, "Cannot find any public G+ profile");
        }
        return player;
    }

    /**
     * Retrieves the {@link it.rainbowbreeze.playtog.domain.Player} for the currently logged
     *  G+ user
     * @return
     */
    public Player getLogged(GoogleApiClient googleApiClient) {
        mLogFacility.v(LOG_TAG, "Getting currently logged G+ user");
        Person currentUser = Plus.PeopleApi.getCurrentPerson(googleApiClient);
        return Player.createFrom(currentUser);
    }
}
