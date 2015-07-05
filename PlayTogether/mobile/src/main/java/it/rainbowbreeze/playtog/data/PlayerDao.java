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

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.CursorLoader;

import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.data.provider.player.PlayerColumns;
import it.rainbowbreeze.playtog.data.provider.player.PlayerContentValues;
import it.rainbowbreeze.playtog.data.provider.player.PlayerCursor;
import it.rainbowbreeze.playtog.data.provider.player.PlayerSelection;
import it.rainbowbreeze.playtog.domain.Player;

/**
 * Created by alfredomorresi on 02/11/14.
 */
public class PlayerDao {
    private static final String LOG_TAG = PlayerDao.class.getSimpleName();

    private final Context mAppContext;
    private final ILogFacility mLogFacility;

    public PlayerDao(Context appContext, ILogFacility logFacility) {
        mAppContext = appContext;
        mLogFacility = logFacility;
    }

    /**
     * Inserts an {@link it.rainbowbreeze.playtog.domain.Player} in the Content Provider
     * @param player
     */
    public void insert(Player player) {
        PlayerContentValues values = new PlayerContentValues();
        player.fillContentValues(values);
        mAppContext.getContentResolver().insert(PlayerColumns.CONTENT_URI, values.values());
    }

    /**
     * Remove all players from the Content Provider
     */
    public void deleteAll() {
        PlayerSelection where = new PlayerSelection();
        mAppContext.getContentResolver().delete(PlayerColumns.CONTENT_URI, where.sel(), where.args());
    }

    /**
     * Returns an {@link it.rainbowbreeze.playtog.domain.Player} given its id
     *
     * @return
     */
    public Player getById(long playerId) {
        PlayerSelection PlayerSelection = new PlayerSelection();
        PlayerSelection.id(playerId);
        return queryForPlayer(PlayerSelection);
    }

    /**
     * Finds if a player already exist in the local database, given is unique id
     * (generally the backend id of the player)
     *
     * @param uniqueId
     * @return
     */
    public boolean exists(String uniqueId) {
        PlayerSelection PlayerSelection = new PlayerSelection();
        PlayerSelection.backendid(uniqueId);
        String projection[] = {PlayerColumns._ID };
        PlayerCursor c = PlayerSelection.query(mAppContext.getContentResolver(), projection);
        boolean found = false;
        while (c.moveToNext()) {
            found = true;
            break;
        }
        c.close();
        return found;
    }

    /**
     * Returns the number of players in a selected state
     * @return
     */
    public int countSelectedPlayers() {
        PlayerSelection PlayerSelection = new PlayerSelection();
        PlayerSelection.selected(true);
        String projection[] = {PlayerColumns._ID };
        PlayerCursor c = PlayerSelection.query(mAppContext.getContentResolver(), projection);
        int total = c.getCount();
        //while (c.moveToNext()) {
        //}
        c.close();
        return total;
    }

    /**
     * Retrieve a particular {@link it.rainbowbreeze.playtog.domain.Player} given the
     *  query parameters
     * @param PlayerSelection
     * @return
     */
    private Player queryForPlayer(PlayerSelection PlayerSelection) {
        PlayerCursor c = PlayerSelection.query(mAppContext.getContentResolver(),
                null,
                PlayerColumns.ACCEPTEDDATE + " DESC");
        Player player = null;
        while (c.moveToNext()) {
            player = Player.createFrom(c);
            break;
        }
        c.close();
        return player;
    }


    /**
     * Returns all the available players for the current match
     * @return
     */
    public CursorLoader getPlayersForTheGame() {
        PlayerSelection where = new PlayerSelection();
        //where.visible(true).and().uploadprogress(Player.UPLOAD_DONE_ALL);
        CursorLoader cursorLoader = new CursorLoader(
                mAppContext,
                PlayerColumns.CONTENT_URI,
                null,
                where.sel(),
                where.args(),
                PlayerColumns.ACCEPTEDDATE + " ASC");
        //Cursor c = mAppContext.getContentResolver().query(PlayerColumns.CONTENT_URI, null,
        //        where.sel(), where.args(), PlayerColumns.DATE + " DESC");
        return cursorLoader;
    }

    /**
     * Updates different fields of an entity, given its id
     * @param playerId
     * @param values
     * @return number of entity updated, 0 if id doesn't exist or 1 if exists
     */
    private int update(long playerId, PlayerContentValues values) {
        Uri playerUri = Uri.parse(PlayerColumns.CONTENT_URI + "/" + playerId);
        return mAppContext.getContentResolver().update(
                playerUri,
                values.values(),
                null,
                null);
    }

    /**
     * Hides a particular player from the list
     * @param playerId
     */
    public int toggleSelectionById(long playerId) {
        Player player = getById(playerId);
        PlayerContentValues values = new PlayerContentValues();
        values.putSelected(!player.isSelected());
        return update(playerId, values);
    }

    public List<String> getSelectedPlayerIds() {
        mLogFacility.v(LOG_TAG, "Listing selected players social ids");
        PlayerSelection where = new PlayerSelection();
        where.selected(true);
        PlayerCursor c = where.query(
                mAppContext.getContentResolver(),
                new String[]{PlayerColumns.SOCIALID});
        List<String> socialIds = new ArrayList<>();
        while (c.moveToNext()) {
            socialIds.add(c.getSocialid());
        }
        c.close();
        return socialIds;
    }
}
