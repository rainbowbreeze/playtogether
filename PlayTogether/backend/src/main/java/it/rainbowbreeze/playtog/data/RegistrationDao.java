/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
   -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

package it.rainbowbreeze.playtog.data;

import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.playtog.domain.RegistrationRecord;

import static it.rainbowbreeze.playtog.data.OfyService.ofy;

/**
 * Created by alfredomorresi on 08/01/15.
 */
public class RegistrationDao extends BaseOfyDao<RegistrationRecord> {

    public RegistrationDao() {
        super(RegistrationRecord.class);
    }

    public List<String> getRegistrationIdsForUser(String userId) {
        List<String> results = new ArrayList<>();

        // Get all the records for the given user id
        List<RegistrationRecord> records = ofy().load().type(RegistrationRecord.class)
                .filter("userId", userId)
                .limit(MAX_COUNT).list();
        for (RegistrationRecord record : records) {
            results.add(record.getRegistrationId());
        }

        return results;
    }

    /**
     * Updated a particular registration id with a new value
     * @param registrationId
     * @param canonicalRegId
     * @return
     */
    public RegistrationRecord updateRegistrationId(String registrationId, String canonicalRegId) {
        RegistrationRecord registration = getFromRegistrationId(registrationId);
        registration.setRegistrationId(canonicalRegId);
        save(registration);
        return registration;
    }

    /**
     * Deletes an entity given it's registration id
     * @param registrationId
     */
    public void removeByRegistrationId(String registrationId) {
        RegistrationRecord registration = getFromRegistrationId(registrationId);
        delete(registration);
    }

    public RegistrationRecord getFromRegistrationId(String registrationId) {
        // Generally, only one entity is present for a given registrationId
        RegistrationRecord registration = ofy().load().type(RegistrationRecord.class)
                .filter("registrationId", registrationId).first().now();
        return registration;
    }

    public List<String> getAllRegistrationIds() {
        // Ok, I know, it isn't the most optimized way of doing
        List<RegistrationRecord> registrations = listAll();
        List<String> registrationIds = new ArrayList<>();
        for (RegistrationRecord registration : registrations) {
            registrationIds.add(registration.getRegistrationId());
        }
        return registrationIds;
    }

}
