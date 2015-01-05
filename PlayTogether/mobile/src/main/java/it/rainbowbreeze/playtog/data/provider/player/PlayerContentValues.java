package it.rainbowbreeze.playtog.data.provider.player;

import java.util.Date;

import android.content.ContentResolver;
import android.net.Uri;

import it.rainbowbreeze.playtog.data.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code player} table.
 */
public class PlayerContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return PlayerColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, PlayerSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public PlayerContentValues putPictureurl(String value) {
        mContentValues.put(PlayerColumns.PICTUREURL, value);
        return this;
    }

    public PlayerContentValues putPictureurlNull() {
        mContentValues.putNull(PlayerColumns.PICTUREURL);
        return this;
    }


    public PlayerContentValues putName(String value) {
        if (value == null) throw new IllegalArgumentException("value for name must not be null");
        mContentValues.put(PlayerColumns.NAME, value);
        return this;
    }



    public PlayerContentValues putSocialid(String value) {
        if (value == null) throw new IllegalArgumentException("value for socialid must not be null");
        mContentValues.put(PlayerColumns.SOCIALID, value);
        return this;
    }



    public PlayerContentValues putBackendid(String value) {
        if (value == null) throw new IllegalArgumentException("value for backendid must not be null");
        mContentValues.put(PlayerColumns.BACKENDID, value);
        return this;
    }



    public PlayerContentValues putSelected(boolean value) {
        mContentValues.put(PlayerColumns.SELECTED, value);
        return this;
    }



    public PlayerContentValues putAccepteddate(Date value) {
        if (value == null) throw new IllegalArgumentException("value for accepteddate must not be null");
        mContentValues.put(PlayerColumns.ACCEPTEDDATE, value.getTime());
        return this;
    }


    public PlayerContentValues putAccepteddate(long value) {
        mContentValues.put(PlayerColumns.ACCEPTEDDATE, value);
        return this;
    }

}
