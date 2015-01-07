/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Backend with Google Cloud Messaging" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/GcmEndpoints
*/

package it.rainbowbreeze.playtog.endpoint;

import com.google.android.gcm.server.Message;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import it.rainbowbreeze.playtog.common.Bag;
import it.rainbowbreeze.playtog.domain.GameRecord;
import it.rainbowbreeze.playtog.logic.GcmMessageHelper;

import static it.rainbowbreeze.playtog.OfyService.ofy;

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
        name = "game",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "playtog.rainbowbreeze.it",
                ownerName = "playtog.rainbowbreeze.it",
                packagePath = "")
)
public class GameEndpoint {
    private static final Logger mLog = Logger.getLogger(GameEndpoint.class.getName());

    /**
     * Starts to ask for players for a new game
     *
     * @param userId
     * @param roomId
     */
    @ApiMethod(name = "searchForPlayers")
    public void searchForPlayers(@Named("userId") String userId, @Named("roomId") String roomId) throws IOException {
        // Stores the new game request
        GameRecord game = new GameRecord()
                .setCallPlayerId(userId)
                .setRoomId(roomId);
        //ofy().save().entity(game).now();
        mLog.info("Saved new game request");

        // Alerts other players for then new game request
        Message message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_SEARCH_FOR_PLAYERS)
                .addData(Bag.EXTRA_PLAYER_ID, game.getCallPlayerId())
                .build();
        GcmMessageHelper messageHelper = new GcmMessageHelper();
        messageHelper.sendMessage(message);
    }

    /**
     * Tell that a player want to participate to a given game
     *
     * @param gameId
     * @param newUserId
     */
    @ApiMethod(name = "participate")
    public void participate(@Named("gameId") String gameId, @Named("userId") String newUserId) throws IOException {
        GameRecord game = getGameRecord(gameId);

        if (null == game) {
            mLog.info("Cannot load the game to participate, aborting");
        }

        Message message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_NEW_USER_FOR_GAME)
                .addData(Bag.EXTRA_PLAYER_ID, newUserId)
                .build();
        GcmMessageHelper messageHelper = new GcmMessageHelper();
        messageHelper.sendMessage(message);
    }


    /**
     * Starts a new game with the given players
     *
     * @param gameId
     * @param players
     */
    @ApiMethod(name = "start")
    public void start(@Named("gameId") String gameId, @Named("players") List<String> players) throws IOException {
        GameRecord game = getGameRecord(gameId);
        if (null == game) {
            mLog.info("Cannot load the game to start, aborting");
        }
        // Reads all the players that asked to participate to a given match

        Message message;
        // Alerts all players that they've been accepted to a given game
        message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_ACCEPTED)
                .build();

        // Alerts all players that they've been refused for a given game
        message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_DENIED)
                .build();

        // Remove game data
    }

    private GameRecord getGameRecord(String id) {
        return ofy().load().type(GameRecord.class).id(id).now();
    }

}
