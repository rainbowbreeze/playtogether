package it.rainbowbreeze.playtog.data.provider.player;

import java.util.Date;

import android.database.Cursor;

import it.rainbowbreeze.playtog.data.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code player} table.
 */
public class PlayerCursor extends AbstractCursor {
    public PlayerCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Get the {@code pictureurl} value.
     * Can be {@code null}.
     */
    public String getPictureurl() {
        Integer index = getCachedColumnIndexOrThrow(PlayerColumns.PICTUREURL);
        return getString(index);
    }

    /**
     * Get the {@code name} value.
     * Cannot be {@code null}.
     */
    public String getName() {
        Integer index = getCachedColumnIndexOrThrow(PlayerColumns.NAME);
        return getString(index);
    }

    /**
     * Get the {@code socialid} value.
     * Cannot be {@code null}.
     */
    public String getSocialid() {
        Integer index = getCachedColumnIndexOrThrow(PlayerColumns.SOCIALID);
        return getString(index);
    }

    /**
     * Get the {@code backendid} value.
     * Can be {@code null}.
     */
    public String getBackendid() {
        Integer index = getCachedColumnIndexOrThrow(PlayerColumns.BACKENDID);
        return getString(index);
    }

    /**
     * Get the {@code selected} value.
     */
    public boolean getSelected() {
        return getBoolean(PlayerColumns.SELECTED);
    }

    /**
     * Get the {@code accepteddate} value.
     * Cannot be {@code null}.
     */
    public Date getAccepteddate() {
        return getDate(PlayerColumns.ACCEPTEDDATE);
    }
}
