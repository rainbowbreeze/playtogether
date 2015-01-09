package it.rainbowbreeze.playtog.domain;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alfredomorresi on 07/01/15.
 */
@Entity
public class GameRecord {

    public GameRecord() {
        playerIds = new ArrayList<>();
    }

    @Id Long id;
    public Long getId() {
        return id;
    }

    /** ID of the player who asked to start the match */
    private String ownerId;
    public String getOwnerId() {
        return ownerId;
    }
    public GameRecord setOwnerId(String newValue) {
        ownerId = newValue;
        return this;
    }

    /** room id of the match */
    @Index private String roomId;
    public String getRoomId() {
        return roomId;
    }
    public GameRecord setRoomId(String newValue) {
        roomId = newValue;
        return this;
    }

    private final List<String> playerIds;
    public List<String> getPlayerIds() {
        return playerIds;
    }
    public GameRecord addPlayer(String newPlayerId) {
        playerIds.add(newPlayerId);
        return this;
    }
}
