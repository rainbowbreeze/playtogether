package it.rainbowbreeze.playtog.logic.actions;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.logic.BackendManager;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class SubscribeClientToGcmAction extends RainbowActionsManager.BaseAction {
    private static final String LOG_TAG = SubscribeClientToGcmAction.class.getSimpleName();

    private final BackendManager mBackendManager;

    protected SubscribeClientToGcmAction(IRainbowLogFacility logFacility, BackendManager backendManager, ActionsManager actionManager) {
        super(logFacility, actionManager);
        mBackendManager = backendManager;
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    protected void doYourStuff() {
        mBackendManager.registerClient();
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
