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


    public ActionsManager(Context appContext, ILogFacility logFacility, BackendManager backendManager) {
        super(logFacility, LOG_TAG);
        mAppContext = appContext;
        mBackendManager = backendManager;
    }
}
