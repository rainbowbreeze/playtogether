package it.rainbowbreeze.playtog.logic.actions;

import android.text.TextUtils;

import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.common.Bag;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.logic.BackendHelper;

/**
 * Created by alfredomorresi on 09/01/15.
 */
public class ParticipateToAGameAction extends RainbowActionsManager.BaseAction {
    private static final String LOG_TAG = ParticipateToAGameAction.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final BackendHelper mBackendHelper;
    private long mGameId = Bag.ID_NOT_SET;

    protected ParticipateToAGameAction(ILogFacility logFacility, BackendHelper backendHelper, RainbowActionsManager actionManager) {
        super(logFacility, actionManager);
        mLogFacility = logFacility;
        mBackendHelper = backendHelper;
    }

    @Override
    protected boolean isDataValid() {
        return Bag.ID_NOT_SET != mGameId;
    }

    @Override
    protected void doYourStuff() {
        mLogFacility.v("Participate to the game " + mGameId);
        if (mBackendHelper.participateToAGame(mGameId)) {
            mLogFacility.v(LOG_TAG, "Successfully applied for the game");
        } else {
            mLogFacility.v(LOG_TAG, "Cannot participate to the game because of a backend error");
        }
    }

    @Override
    protected ConcurrencyType getConcurrencyType() {
        return ConcurrencyType.SingleInstance;
    }

    @Override
    protected String getUniqueActionId() {
        return LOG_TAG;
    }

    @Override
    protected String getLogTag() {
        return LOG_TAG;
    }

    public ParticipateToAGameAction setGameId(long newValue) {
        mGameId = newValue;
        return this;
    }
}
