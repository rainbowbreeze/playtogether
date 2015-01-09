package it.rainbowbreeze.playtog.domain;

/**
 * Created by alfredomorresi on 08/01/15.
 */
public abstract class BaseResult {
    protected final boolean mSuccess;
    protected String mErrorMessage;

    protected BaseResult(boolean success) {
        mSuccess = success;
    }
    protected BaseResult(String errorMessage) {
        this(false);
        mErrorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return mSuccess;
    }

    public String getErrorMessage() {
        return mErrorMessage;
    }

}
