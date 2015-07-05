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

package it.rainbowbreeze.playtog.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * The Objectify object model for device registrations we are persisting
 */
@Entity
public class RegistrationRecord {

    public RegistrationRecord() {
    }

    @Id Long id;

    @Index private String registrationId;
    public String getRegistrationId() {
        return registrationId;
    }
    public RegistrationRecord setRegistrationId(String newValue) {
        registrationId = newValue;
        return this;
    }

    @Index private String userId;
    public String getUserId() {
        return userId;
    }
    public RegistrationRecord setUserId(String newValue) {
        userId = newValue;
        return this;
    }

    //TODO: add a when field, so cleanup of old ids could be performed
    // in a background task once in a while

}
