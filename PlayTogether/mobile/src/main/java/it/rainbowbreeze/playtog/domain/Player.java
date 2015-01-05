package it.rainbowbreeze.playtog.domain;

import java.util.Date;

import it.rainbowbreeze.playtog.data.provider.player.PlayerContentValues;
import it.rainbowbreeze.playtog.data.provider.player.PlayerCursor;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class Player {

    public Player() {
        setSocialId("test-social");
        setBackendId("test-backend");
        setAcceptedDate(new Date());
    }

    private long mId;
    public long getId() {
        return mId;
    }
    private Player setId(long newValue) {
        mId = newValue;
        return this;
    }

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

    private String mSocialId;
    public String getSocialId() {
        return mSocialId;
    }
    private Player setSocialId(String socialId) {
        mSocialId = socialId;
        return this;
    }

    private String mBackendId;
    public String getBackendId() {
        return mBackendId;
    }
    public Player setBackendId(String backendId) {
        mBackendId = backendId;
        return this;
    }

    private Date mAcceptedDate;
    public Date getAcceptedDate() {
        return mAcceptedDate;
    }
    private Player setAcceptedDate(Date backendId) {
        mAcceptedDate = backendId;
        return this;
    }

    public static Player fromCursor(PlayerCursor c) {
        return new Player()
                .setAcceptedDate(c.getAccepteddate())
                .setBackendId(c.getBackendid())
                .setId(c.getId())
                .setName(c.getName())
                .setPictureUrl(c.getPictureurl())
                .setSelected(c.getSelected())
                .setSocialId(c.getSocialid());
    }

    public void fillContentValues(PlayerContentValues values) {
        values
                .putAccepteddate(getAcceptedDate())
                .putBackendid(getBackendId())
                .putName(getName())
                .putPictureurl(getPictureUrl())
                .putSelected(isSelected())
                .putSocialid(getSocialId());
    }
}
