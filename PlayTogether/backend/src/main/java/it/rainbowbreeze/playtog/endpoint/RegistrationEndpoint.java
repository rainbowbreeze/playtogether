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

/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package it.rainbowbreeze.playtog.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.api.server.spi.response.CollectionResponse;

import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import it.rainbowbreeze.playtog.common.Bag;
import it.rainbowbreeze.playtog.data.RegistrationDao;
import it.rainbowbreeze.playtog.domain.RegistrationRecord;

/**
 * A registration endpoint class we are exposing for a device's GCM registration id on the backend
 * <p/>
 * For more information, see
 * https://developers.google.com/appengine/docs/java/endpoints/
 * <p/>
 * NOTE: This endpoint does not use any form of authorization or
 * authentication! If this app is deployed, anyone can access this endpoint! If
 * you'd like to add authentication, take a look at the documentation.
 */
@Api(
        name = "registration",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = Bag.API_OWNER_DOMAIN,
                ownerName = Bag.API_OWNER_NAME,
                packagePath = Bag.API_PACKAGE_PATH)
)
public class RegistrationEndpoint {
    private static final Logger mLog = Logger.getLogger(RegistrationEndpoint.class.getName());

    private final RegistrationDao mRegistrationDao;

    public RegistrationEndpoint() {
        mRegistrationDao = new RegistrationDao();
    }

    /**
     * Register a device to the backend
     *
     * @param registrationId The Google Cloud Messaging registration Id to add
     * @param userId The userId that is using the device
     */
    @ApiMethod(name = "register")
    public void registerDevice(
            @Named("registrationId") String registrationId,
            @Named("userId") String userId
    ) {
        RegistrationRecord registration = mRegistrationDao.getFromRegistrationId(registrationId);
        if (registration != null && registration.getUserId().equals(userId)) {
            mLog.info("Device " + registrationId + " already registered with the same user, skipping register");
            return;
        }
        if (registration != null) {
            // Deletes old record, probably another user logged to the same device
            mRegistrationDao.delete(registration);
        }

        // Adds the new record
        RegistrationRecord record = new RegistrationRecord()
                .setRegistrationId(registrationId)
                .setUserId(userId);
        mRegistrationDao.save(record);
        mLog.info("Registered new device: " + registrationId + " for user id " + userId);
    }

    /**
     * Unregister a device from the backend
     *
     * @param registrationId The Google Cloud Messaging registration Id to remove
     */
    @ApiMethod(name = "unregister")
    public void unregisterDevice(
            @Named("registrationId") String registrationId
    ) {
        RegistrationRecord record = mRegistrationDao.getFromRegistrationId(registrationId);
        if (record == null) {
            mLog.info("Device " + registrationId + " not registered, skipping unregister");
            return;
        }
        mRegistrationDao.delete(record);
        mLog.info("Unregistered device: " + registrationId);
    }

    /**
     * Return a collection of registered devices
     *
     * @param count The number of devices to list
     * @return a list of Google Cloud Messaging registration Ids
     */
    @ApiMethod(name = "listDevices")
    public CollectionResponse<RegistrationRecord> listDevices(
            @Named("count") int count
    ) {
        List<RegistrationRecord> records = mRegistrationDao.listAll();
        return CollectionResponse.<RegistrationRecord>builder().setItems(records).build();
    }

    /**
     * Deletes all registered devices. USE WITH CARE!!!
     */
    @ApiMethod(name = "deleteAll")
    public void deleteAll() {
        mLog.info("Deleting all registered devices");
        mRegistrationDao.deleteAll();
    }
}
