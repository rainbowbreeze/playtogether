package it.rainbowbreeze.playtog.domain;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class Player {
    private String mName;
    public String getName() {
        return mName;
    }
    public Player setName(String newValue) {
        mName = newValue;
        return this;
    }

    private String mPictureUrl;
    public String getPictureUrl() {
        return mPictureUrl;
    }
    public Player setPictureUrl(String newValue) {
        mPictureUrl = newValue;
        return this;
    }

    private boolean mSelected;
    public boolean isSelected() {
        return mSelected;
    }
    public Player setSelected(boolean newValue) {
        mSelected = newValue;
        return this;
    }
    public Player toggleSelected() {
        mSelected = !mSelected;
        return this;
    }

}
