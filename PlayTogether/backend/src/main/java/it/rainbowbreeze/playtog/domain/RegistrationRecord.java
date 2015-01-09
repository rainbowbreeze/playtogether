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

}