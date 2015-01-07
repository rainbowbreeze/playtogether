package it.rainbowbreeze.playtog.domain;

import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class GameRecord {

    public GameRecord() {
    }

    @Id
    Long id;
    public Long getId() {
        return id;
    }

    /** ID of the player who asked to start the match */
    private String callPlayerId;
    public String getCallPlayerId() {
        return callPlayerId;
    }
    public GameRecord setCallPlayerId(String newValue) {
        callPlayerId = newValue;
        return this;
    }

    /** ID of room of the match */
    @Index
    private String roomId;
    public String getRoomId() {
        return roomId;
    }
    public GameRecord setRoomId(String newValue) {
        roomId = newValue;
        return this;
    }
}
