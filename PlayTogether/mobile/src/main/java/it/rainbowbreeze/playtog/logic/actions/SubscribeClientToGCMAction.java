package it.rainbowbreeze.playtog.logic.actions;

import it.rainbowbreeze.libs.common.IRainbowLogFacility;
import it.rainbowbreeze.libs.logic.RainbowActionsManager;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class SubscribeClientToGCMAction extends RainbowActionsManager.BaseAction {
    protected SubscribeClientToGCMAction(IRainbowLogFacility logFacility, ActionsManager actionManager) {
        super(logFacility, actionManager);
    }

    @Override
    protected boolean isDataValid() {
        return false;
    }

    @Override
    protected void doYourStuff() {

    }

    @Override
    protected ConcurrencyType getConcurrencyType() {
        return null;
    }

    @Override
    protected String getUniqueActionId() {
        return null;
    }

    @Override
    protected String getLogTag() {
        return null;
    }
}
