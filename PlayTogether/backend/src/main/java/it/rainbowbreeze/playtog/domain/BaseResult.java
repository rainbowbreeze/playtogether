package it.rainbowbreeze.playtog.domain;

/**
 * Created by alfredomorresi on 08/01/15.
 */
public abstract class BaseResult {
    protected final boolean mSuccess;

    protected BaseResult(boolean success) {
        mSuccess = success;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

}
