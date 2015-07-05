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

package it.rainbowbreeze.playtog.logic;

import com.google.android.gcm.server.Constants;
import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import it.rainbowbreeze.playtog.data.RegistrationDao;
import it.rainbowbreeze.playtog.domain.RegistrationRecord;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class GcmMessageHelper {
    private static final Logger mLog = Logger.getLogger(GcmMessageHelper.class.getName());

    /**
     * Api Keys can be obtained from the google cloud console
     */
    private static final String API_KEY = System.getProperty("gcm.api.key");

    private final RegistrationDao mRegistrationDao;
    private final Sender mSender;

    public GcmMessageHelper() {
        mRegistrationDao = new RegistrationDao();
        mSender = new Sender(API_KEY);
    }

    /**
     * Send messages to all the registered devices (within the max limit)
     * @param message
     * @throws IOException
     */
    public void sendMessage(Message message) throws IOException {
        sendMessage(mRegistrationDao.getAllRegistrationIds(), message);
    }

    public void sendMessage(List<String> registrationIds, Message message) throws IOException  {
        mLog.info("Sending to " + registrationIds.size() + " clients message " + message);
        for (String registrationId : registrationIds) {
            // sendNoReply otherwise a device switched off will receive the message the same
            Result result = mSender.sendNoRetry(message, registrationId);
            if (result.getMessageId() != null) {
                mLog.info("Message sent to " + registrationId);
                String canonicalRegId = result.getCanonicalRegistrationId();
                if (canonicalRegId != null) {
                    // if the regId changed, we have to update the datastore
                    mLog.info("Registration Id changed for " + registrationId + " updating to " + canonicalRegId);
                    mRegistrationDao.updateRegistrationId(registrationId, canonicalRegId);
                }
            } else {
                String error = result.getErrorCodeName();
                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
                    mLog.warning("Registration Id " + registrationId + " no longer registered with GCM, removing from datastore");
                    // if the device is no longer registered with Gcm, remove it from the datastore
                    mRegistrationDao.removeByRegistrationId(registrationId);
                } else {
                    mLog.warning("Error when sending message : " + error);
                }
            }
        }
    }
}
