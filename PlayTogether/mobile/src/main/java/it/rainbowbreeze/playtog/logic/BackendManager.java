package it.rainbowbreeze.playtog.logic;

import it.rainbowbreeze.playtog.common.ILogFacility;

/**
 * Created by alfredomorresi on 06/01/15.
 */
public class BackendManager {
    private static final String LOG_TAG = BackendManager.class.getSimpleName();

    private final ILogFacility mLogFacility;

    public BackendManager(ILogFacility logFacility) {
        mLogFacility = logFacility;
    }
}
