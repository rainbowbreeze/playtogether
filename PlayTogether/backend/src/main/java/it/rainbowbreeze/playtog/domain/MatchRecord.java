package it.rainbowbreeze.playtog.domain;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class MatchRecord {

    public MatchRecord() {
    }

    @Id
    Long id;

    /** ID of the player who asked to start the match */
    private String callPlayerId;
    public String getCallPlayerId() {
        return callPlayerId;
    }
    public void setCallPlayerId(String newValue) {
        callPlayerId = newValue;
    }

    /** ID of room of the match */
    @Index
    private String roomId;
}
