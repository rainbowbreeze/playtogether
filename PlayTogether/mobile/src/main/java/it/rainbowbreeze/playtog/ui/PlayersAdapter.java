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

package it.rainbowbreeze.playtog.ui;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.data.provider.player.PlayerCursor;
import it.rainbowbreeze.playtog.domain.Player;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class PlayersAdapter
        extends CursorAdapter {
    private static final String LOG_TAG = PlayersAdapter.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final LayoutInflater mInflater;

    public PlayersAdapter(
            Context context,
            Cursor c,
            boolean autoRequery,
            ILogFacility logFacility) {
        super(context, c, autoRequery);
        mInflater = LayoutInflater.from(context);
        mLogFacility = logFacility;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        mLogFacility.v(LOG_TAG, "Creating placeholder");
        ViewHolder holder = new ViewHolder();
        View newView = mInflater.inflate(R.layout.vw_player_item, parent, false);
        holder.userName = (TextView) newView.findViewById(R.id.playeritem_lblPlayerName);
        holder.imgPicture = (CircleImageView) newView.findViewById(R.id.playeritem_imgProfile);
        holder.selected = newView.findViewById(R.id.playeritem_imgSelected);
        newView.setTag(holder);
        return newView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder holder = (ViewHolder) view.getTag();
        PlayerCursor playerCursor = new PlayerCursor(cursor);
        Player player = Player.createFrom(playerCursor);
        holder.userName.setText(player.getName());
        holder.selected.setVisibility(player.isSelected() ? View.VISIBLE : View.INVISIBLE);
        if (!TextUtils.isEmpty(player.getPictureUrl())) {
            Picasso.with(holder.imgPicture.getContext())
                    .load(player.getPictureUrl())
                    .noFade()  // Required by CircleImageView
                    .into(holder.imgPicture);
        } else {
            holder.imgPicture.setImageResource(R.drawable.ic_noprofile);
        }
    }

    /**
     * ViewHolder pattern
     */
    private static class ViewHolder {
        public TextView userName;
        public CircleImageView imgPicture;
        public View selected;
    }
}
