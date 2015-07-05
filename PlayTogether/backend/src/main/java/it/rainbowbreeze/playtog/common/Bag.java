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

package it.rainbowbreeze.playtog.common;

/**
 * Created by alfredomorresi on 07/01/15.
 */
public class Bag {

    // Need to match with the corresponding class in the mobile module
    public static final String EXTRA_MESSAGE = "Message";
    public static final String EXTRA_GCMACTION_TYPE = "GcmActionType";
    public static final String EXTRA_PLAYER_ID = "PlayerId";
    public static final String EXTRA_ROOM_ID = "RoomId";
    public static final String EXTRA_GAME_ID = "GameId";

    // Needs to match with the corresponding class in the mobile module
    public static final String GCMACTION_SEARCH_FOR_PLAYERS = "SearchForPlayers";
    public static final String GCMACTION_ACCEPTED = "Accepted";
    public static final String GCMACTION_DENIED = "Denied";
    public static final String GCMACTION_NEW_USER_FOR_GAME = "NewUserForGame";

    public static final String API_OWNER_DOMAIN = "playtog.rainbowbreeze.it";
    public static final String API_OWNER_NAME = "playtog.rainbowbreeze.it";
    public static final String API_PACKAGE_PATH = "";
}
