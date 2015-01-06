package it.rainbowbreeze.playtog.logic.actions;

import android.content.Context;

import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.logic.BackendManager;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class ActionsManager extends RainbowActionsManager {
    private static final String LOG_TAG = ActionsManager.class.getSimpleName();
    private final Context mAppContext;
    private final BackendManager mBackendManager;
    private final ILogFacility mLogFacility;


    public ActionsManager(Context appContext, ILogFacility logFacility, BackendManager backendManager) {
        super(logFacility, LOG_TAG);
        mAppContext = appContext;
        mLogFacility = logFacility;
        mBackendManager = backendManager;
    }

    public SubscribeClientToGCMActions SubscribeClientToGcm() {
        return new SubscribeClientToGCMActions(mLogFacility, mBackendManager, this);
    }

    public UnsubscribeClientFromGCMActions UnsubscribeClientToGcm() {
        return new UnsubscribeClientFromGCMActions(mLogFacility, mBackendManager, this);
    }

}
