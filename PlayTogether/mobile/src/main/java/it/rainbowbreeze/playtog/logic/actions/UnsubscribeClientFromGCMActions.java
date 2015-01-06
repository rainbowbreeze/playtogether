package it.rainbowbreeze.playtog.logic.actions;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.logic.BackendManager;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class UnsubscribeClientFromGCMActions extends RainbowActionsManager.BaseAction {
    private static final String LOG_TAG = UnsubscribeClientFromGCMActions.class.getSimpleName();

    private final BackendManager mBackendManager;

    protected UnsubscribeClientFromGCMActions(IRainbowLogFacility logFacility, BackendManager backendManager, ActionsManager actionManager) {
        super(logFacility, actionManager);
        mBackendManager = backendManager;
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    protected void doYourStuff() {
        mBackendManager.unregisterClient();
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
