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

package it.rainbowbreeze.playtog.logic.actions;

import android.content.Context;

import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.logic.BackendHelper;
import it.rainbowbreeze.playtog.logic.GameManager;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class ActionsManager extends RainbowActionsManager {
    private static final String LOG_TAG = ActionsManager.class.getSimpleName();
    private final Context mAppContext;
    private final ILogFacility mLogFacility;
    private final BackendHelper mBackendHelper;
    private final GameManager mGameManager;

    public ActionsManager(Context appContext, ILogFacility logFacility, BackendHelper backendHelper, GameManager gameManager) {
        super(logFacility, LOG_TAG);
        mAppContext = appContext;
        mLogFacility = logFacility;
        mBackendHelper = backendHelper;
        mGameManager = gameManager;
    }

    public SubscribeClientToGcmAction subscribeClientToGcm() {
        return new SubscribeClientToGcmAction(mLogFacility, mBackendHelper, this);
    }

    public UnsubscribeClientFromGcmAction unsubscribeClientToGcm() {
        return new UnsubscribeClientFromGcmAction(mLogFacility, mBackendHelper, this);
    }

    public ParticipateToAGameAction participateToAGame() {
        return new ParticipateToAGameAction(mLogFacility, mBackendHelper, this);
    }

    public StartTheGameAction startTheGame() {
        return new StartTheGameAction(mLogFacility, mGameManager, this);
    }

}
