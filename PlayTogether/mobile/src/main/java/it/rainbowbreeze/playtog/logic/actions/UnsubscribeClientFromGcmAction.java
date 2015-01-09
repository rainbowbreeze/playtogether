package it.rainbowbreeze.playtog.logic.actions;

import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.logic.BackendHelper;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class UnsubscribeClientFromGcmAction extends RainbowActionsManager.BaseAction {
    private static final String LOG_TAG = UnsubscribeClientFromGcmAction.class.getSimpleName();

    private final BackendHelper mBackendHelper;

    protected UnsubscribeClientFromGcmAction(ILogFacility logFacility, BackendHelper backendHelper, ActionsManager actionManager) {
        super(logFacility, actionManager);
        mBackendHelper = backendHelper;
    }

    @Override
    protected boolean isDataValid() {
        return true;
    }

    @Override
    protected void doYourStuff() {
        mBackendHelper.unregisterClient();
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
