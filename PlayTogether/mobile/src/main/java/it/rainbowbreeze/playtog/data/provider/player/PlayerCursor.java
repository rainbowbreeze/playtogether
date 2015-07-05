/*
 * Copyright 2015 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
   -+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+ */

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
