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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.inject.Named;

import it.rainbowbreeze.playtog.common.Bag;
import it.rainbowbreeze.playtog.data.GameDao;
import it.rainbowbreeze.playtog.data.RegistrationDao;
import it.rainbowbreeze.playtog.domain.BaseResult;
import it.rainbowbreeze.playtog.domain.GameRecord;
import it.rainbowbreeze.playtog.logic.GcmMessageHelper;

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
                ownerDomain = Bag.API_OWNER_DOMAIN,
                ownerName = Bag.API_OWNER_NAME,
                packagePath = Bag.API_PACKAGE_PATH)
)
public class GameEndpoint {
    private static final Logger mLog = Logger.getLogger(GameEndpoint.class.getName());

    private final GameDao mGameDao;
    private final RegistrationDao mRegistrationDao;
    private final GcmMessageHelper mGcmMessageHelper;

    public GameEndpoint() {
        mGameDao = new GameDao();
        mRegistrationDao = new RegistrationDao();
        mGcmMessageHelper = new GcmMessageHelper();
    }

    public static class GameResult extends BaseResult {
        private final long mGameId;

        public static GameResult OK_RESULT = new GameResult(true);

        public GameResult(long gameId) {
            super(true);
            mGameId = gameId;
        }

        private GameResult(String errorMessage) {
            super(errorMessage);
            mGameId = -1;
        }

        private GameResult(boolean wentWell) {
            super(wentWell);
            mGameId = -1;
        }

        public long getGameId() {
            return mGameId;
        }

        public static GameResult buildWithError(String errorMessage) {
            return new GameResult(errorMessage);
        }
    }

    /**
     * Starts to ask for players for a new game
     *
     * @param ownerId the user id that asked for the new game
     * @param roomId the room id of the new game
     *
     * @return
     */
    @ApiMethod(name = "searchForPlayers")
    public GameResult searchForPlayers(
            @Named("ownerId") String ownerId,
            @Named("roomId") String roomId
    ) throws IOException {
        // Stores the new game request
        GameRecord game = new GameRecord()
                .setOwnerId(ownerId)
                .setRoomId(roomId);
        mGameDao.save(game);
        mLog.info("Saved new game request with game id " + game.getId());

        // Alerts other players for then new game request
        Message message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_SEARCH_FOR_PLAYERS)
                .addData(Bag.EXTRA_PLAYER_ID, game.getOwnerId())
                .addData(Bag.EXTRA_ROOM_ID, game.getRoomId())
                .addData(Bag.EXTRA_GAME_ID, String.valueOf(game.getId()))
                .build();
        //TODO filters by room
        mGcmMessageHelper.sendMessage(message);

        return new GameResult(game.getId());
    }

    /**
     * Tell that a player want to participate to a given game
     *
     * @param gameId
     * @param newUserId
     */
    @ApiMethod(name = "participate")
    public GameResult participate(
            @Named("gameId") long gameId,
            @Named("userId") String newUserId
    ) throws IOException {
        GameRecord game = mGameDao.get(gameId);

        if (null == game) {
            mLog.info("Cannot load the game to participate, aborting");
            return GameResult.buildWithError("Cannot load the game to participate, aborting");
        }

        // Adds the new player to the collection of players for a given game request
        game.addPlayer(newUserId);
        mGameDao.save(game);

        // Retrieves the game owner, so she can be used alerted of the new player
        String ownerId = game.getOwnerId();
        // Retrieves list of devices to send the notification
        List<String> devices = mRegistrationDao.getRegistrationIdsForUser(ownerId);
        // Sends a notification to game owner about the new player
        Message message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_NEW_USER_FOR_GAME)
                .addData(Bag.EXTRA_PLAYER_ID, newUserId)
                .build();
        GcmMessageHelper messageHelper = new GcmMessageHelper();
        messageHelper.sendMessage(devices, message);

        return new GameResult(gameId);
    }


    /**
     * Starts a new game with the given players
     *
     * @param gameId
     * @param playerIds
     */
    // If not specified the right path (omitting playerIds from the path), usage of endpoints in
    //  Android will generate an error like:
    //    com.google.appengine.repackaged.org.codehaus.jackson.map.JsonMappingException:
    //    Can not deserialize instance of java.lang.String[] out of VALUE_STRING token
    @ApiMethod(
            name = "start",
            path = "start/{gameId}"
    )
    public GameResult start(
            @Named("gameId") long gameId,
            @Named("playerIds") List<String> playerIds
    ) throws IOException {
        GameRecord game = mGameDao.get(gameId);
        if (null == game) {
            mLog.info("Cannot load the game to start for the id " + gameId);
            return GameResult.buildWithError("Cannot load the game to start for the id " + gameId);
        }

        if (null == playerIds || playerIds.size() == 0) {
            mLog.info("Cannot start a game with no players");
            return GameResult.buildWithError("Cannot start a game with no players");
        }

        // First of all, checks if accepted players really accepted the match
        List<String> missingPlayerIds = new ArrayList<>();
        for (String playerId : playerIds) {
            if (game.getPlayerIds().contains(playerId)) {
                missingPlayerIds.add(playerId);
            }
        }
        if (missingPlayerIds.size() > 0) {
            mLog.info("Strange, there are " + missingPlayerIds.size() + " player(s) accepted that haven't registered for the game: " + missingPlayerIds.toString());
        }

        Message message;
        // Alerts all players that they've been accepted to a given game
        message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_ACCEPTED)
                .addData(Bag.EXTRA_ROOM_ID, game.getRoomId())
                .build();
        for (String playerId : playerIds) {
            // Send a message for every device registered for a given player
            List<String> registrationIds = mRegistrationDao.getRegistrationIdsForUser(playerId);
            mGcmMessageHelper.sendMessage(registrationIds, message);
        }

        // Alerts all players that they've been refused for a given game
        message = new Message.Builder()
                .addData(Bag.EXTRA_GCMACTION_TYPE, Bag.GCMACTION_DENIED)
                .addData(Bag.EXTRA_ROOM_ID, game.getRoomId())
                .build();
        // Removes all accepted players from the list of players for a given game
        List<String> excludedPlayerIds = game.getPlayerIds();  // Copy, not in that way :(
        for (String playerId : playerIds) {
            excludedPlayerIds.remove(playerId);
        }
        // Communicated to the excluded players the bad news
        for (String excludedPlayerId : excludedPlayerIds) {
            // Send a message for every device registered for a given player
            List<String> registrationIds = mRegistrationDao.getRegistrationIdsForUser(excludedPlayerId);
            mGcmMessageHelper.sendMessage(registrationIds, message);
        }

        // Deletes the game record
        mGameDao.delete(game);
        return GameResult.OK_RESULT;
    }

}
