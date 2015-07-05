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
        mContentValues.put(PlayerColumns.BACKENDID, value);
        return this;
    }

    public PlayerContentValues putBackendidNull() {
        mContentValues.putNull(PlayerColumns.BACKENDID);
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
