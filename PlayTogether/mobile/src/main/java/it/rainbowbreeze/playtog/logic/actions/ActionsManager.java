package it.rainbowbreeze.playtog.logic.actions;

import android.content.Context;

import it.rainbowbreeze.libs.logic.RainbowActionsManager;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.logic.BackendHelper;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class ActionsManager extends RainbowActionsManager {
    private static final String LOG_TAG = ActionsManager.class.getSimpleName();
    private final Context mAppContext;
    private final BackendHelper mBackendHelper;
    private final ILogFacility mLogFacility;


    public ActionsManager(Context appContext, ILogFacility logFacility, BackendHelper backendHelper) {
        super(logFacility, LOG_TAG);
        mAppContext = appContext;
        mLogFacility = logFacility;
        mBackendHelper = backendHelper;
    }

    public SubscribeClientToGcmAction SubscribeClientToGcm() {
        return new SubscribeClientToGcmAction(mLogFacility, mBackendHelper, this);
    }

    public UnsubscribeClientFromGcmAction UnsubscribeClientToGcm() {
        return new UnsubscribeClientFromGcmAction(mLogFacility, mBackendHelper, this);
    }

}
