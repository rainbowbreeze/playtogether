package it.rainbowbreeze.playtog.logic.actions;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.logic.GameManager;

/**
 * Created by alfredomorresi on 09/01/15.
 */
public class StartTheGameAction extends RainbowActionsManager.BaseAction {
    private static final String LOG_TAG = StartTheGameAction.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final GameManager mGameManager;

    protected StartTheGameAction(ILogFacility logFacility, GameManager gameManager, RainbowActionsManager actionManager) {
        super(logFacility, actionManager);
        mLogFacility = logFacility;
        mGameManager = gameManager;
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    protected void doYourStuff() {
        mLogFacility.v(LOG_TAG, "Starting the game");
        mGameManager.startTheGame();
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
}
