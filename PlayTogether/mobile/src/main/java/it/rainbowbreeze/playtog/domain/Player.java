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

package it.rainbowbreeze.playtog.domain;

import android.text.TextUtils;

import com.google.android.gms.plus.model.people.Person;

import java.util.Date;

import it.rainbowbreeze.playtog.data.provider.player.PlayerContentValues;
import it.rainbowbreeze.playtog.data.provider.player.PlayerCursor;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class Player {

    public Player() {
        setSocialId("test-social");
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
    public Player setSocialId(String socialId) {
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
    public Player setAcceptedDate(Date backendId) {
        mAcceptedDate = backendId;
        return this;
    }

    public static Player createFrom(PlayerCursor c) {
        return new Player()
                .setAcceptedDate(c.getAccepteddate())
                .setBackendId(c.getBackendid())
                .setId(c.getId())
                .setName(c.getName())
                .setPictureUrl(c.getPictureurl())
                .setSelected(c.getSelected())
                .setSocialId(c.getSocialid());
    }

    public static Player createFrom(Person person) {
        if (null == person) return null;
        Player player = new Player();
        player.setBackendId("");
        if (!TextUtils.isEmpty(person.getDisplayName())) {
            player.setName(person.getDisplayName());
        } else if (null != person.getName() && !TextUtils.isEmpty(person.getName().toString())) {
            player.setName(person.getName().toString());
        } else if (!TextUtils.isEmpty(person.getNickname())) {
            player.setName(person.getNickname());
        } else {
            player.setName("Unknown name");
        }
        if (null != person.getImage()) {
            player.setPictureUrl(person.getImage().getUrl());
            //Workaround to obtain larger profile picture
            player.setPictureUrl(player.getPictureUrl().replace("?sz=50", "?sz=300"));
        }
        player
                .setSelected(false)
                .setSocialId(person.getId());
        return player;
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
