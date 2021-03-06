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

import android.content.Intent;
import android.text.TextUtils;

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.common.Bag;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.data.PlayerDao;
import it.rainbowbreeze.playtog.domain.Player;
import it.rainbowbreeze.playtog.ui.JoinGameActivity;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class GPlusCommunicationService extends GoogleApiClientBaseService {
    private static final String LOG_TAG = GPlusCommunicationService.class.getSimpleName();

    public static final String ACTION_SEARCH_FOR_PLAYERS = "it.rainbowbreeze.playtog.Action.Plus.SearchForPlayers";
    public static final String ACTION_LOAD_CURRENT_USER = "it.rainbowbreeze.playtog.Action.Plus.LoadCurrentUser";
    public static final String ACTION_ADD_USER_TO_GAME = "it.rainbowbreeze.playtog.Action.Plus.AddUserToGame";
    public static final String EXTRA_USER_ID = "Extra.UserId";
    public static final String EXTRA_ROOM_ID = "Extra.RoomId";
    public static final String EXTRA_GAME_ID = "Extra.GameId";

    @Inject ILogFacility mLogFacility;
    @Inject PlayerDao mPlayerDao;
    @Inject AppPrefsManager mAppPrefsManager;
    @Inject GPlusHelper mGPlusHelper;

    public GPlusCommunicationService() {
        super(LOG_TAG, LOG_TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((MyApp) getApplication()).inject(this);
    }

    @Override
    protected Api getApi() {
        return Plus.API;
    }

    @Override
    protected ILogFacility getLogFacility() {
        return mLogFacility;
    }

    @Override
    protected String[] getValidIntentActions() {
        return new String[] {ACTION_SEARCH_FOR_PLAYERS, ACTION_LOAD_CURRENT_USER, ACTION_ADD_USER_TO_GAME};
    }

    @Override
    protected GoogleApiClient.Builder createGoogleApiClient() {
        return super.createGoogleApiClient().addScope(Plus.SCOPE_PLUS_PROFILE);
    }

    @Override
    public void doYourStuff(Intent intent) {
        String userId = intent.getStringExtra(EXTRA_USER_ID);
        if (TextUtils.isEmpty(userId)) {
            mLogFacility.i(LOG_TAG, "Cannot find user id in the intent, aborting...");
            return;
        }

        if (ACTION_LOAD_CURRENT_USER.equals(intent.getAction())) {
            mLogFacility.v(LOG_TAG, "Loading current user info");
            // Retrieve some profile information to personalize our app for the user.
            Player player = mGPlusHelper.getLogged(mGoogleApiClient);
            if (null != player) {
                mAppPrefsManager.setCurrentPlayer(player);
                mLogFacility.v(LOG_TAG, "Logged user stored as a player");
            } else {
                mLogFacility.v(LOG_TAG, "Cannot store logged G+ user as a player");
            }

        } else if (ACTION_ADD_USER_TO_GAME.equals(intent.getAction())) {
            Player player = mGPlusHelper.get(mGoogleApiClient, userId);
            if (null != player) {
                mPlayerDao.insert(player);
                mLogFacility.v(LOG_TAG, "Added to database G+ person " + player.getName());
            } else {
                mLogFacility.v(LOG_TAG, "Cannot add any G+ person to db because she wasn't found");
            }

        } else if (ACTION_SEARCH_FOR_PLAYERS.equals(intent.getAction())) {
            // Retrieves asking user details
            String roomId = intent.getStringExtra(EXTRA_ROOM_ID);
            long gameId = intent.getLongExtra(EXTRA_GAME_ID, Bag.ID_NOT_SET);
            Player askingPlayer = mGPlusHelper.get(mGoogleApiClient, userId);
            if (null != askingPlayer) {
                mLogFacility.v(LOG_TAG, askingPlayer.getName() + " has asked to join a new game for room " + roomId);
                Intent startIntent = new Intent(getApplicationContext(), JoinGameActivity.class);
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startIntent.putExtra(JoinGameActivity.EXTRA_PLAYER_NAME, askingPlayer.getName());
                startIntent.putExtra(JoinGameActivity.EXTRA_PLAYER_PICTURE_URL, askingPlayer.getPictureUrl());
                startIntent.putExtra(JoinGameActivity.EXTRA_ROOM_ID, roomId);
                startIntent.putExtra(JoinGameActivity.EXTRA_GAME_ID, gameId);
                startActivity(startIntent);
            } else {
                mLogFacility.i(LOG_TAG, "A request for players has arrived, but is impossible to find the asking player");
            }
        }
    }
}
