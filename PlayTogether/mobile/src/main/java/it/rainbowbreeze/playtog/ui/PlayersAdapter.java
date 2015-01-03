package it.rainbowbreeze.playtog.ui;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.domain.Player;
import it.rainbowbreeze.playtog.logic.MatchManager;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class PlayersAdapter
        extends ArrayAdapter<Player> {
    private static final String LOG_TAG = PlayersAdapter.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final MatchManager mMatchManager;
    private final OnPlayersStatusChangedListener mOnPlayersStatusChangedListener;

    public PlayersAdapter(
            Context context,
            ILogFacility logFacility,
            MatchManager matchManager,
            OnPlayersStatusChangedListener onPlayersStatusChangedListener
    ) {
        super(context, R.layout.vw_player_item, matchManager.getPlayers());
        mLogFacility = logFacility;
        mMatchManager = matchManager;
        mOnPlayersStatusChangedListener = onPlayersStatusChangedListener;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        mLogFacility.v(LOG_TAG, "getView for position " + position + " and convertView " +
                (null == convertView ? "null" : convertView.getClass().getSimpleName()));
        ViewHolder holder;
        if (null == convertView) {
            mLogFacility.v(LOG_TAG, "Creating placeholder");
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.vw_player_item, parent, false);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.playeritem_lblPlayerName);
            holder.imgPicture = (CircleImageView) convertView.findViewById(R.id.playeritem_imgProfile);
            holder.selected = convertView.findViewById(R.id.playeritem_imgSelected);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ViewHolder holder = (ViewHolder) v.getTag();
                    mLogFacility.v(LOG_TAG, "Clicked on item " + holder.position);
                    mMatchManager.togglePlayerSelection(holder.position);
                    setItemAppearence(holder);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        Player player = getItem(position);
        holder.position = position;
        holder.userName.setText(player.getName());
        if (!TextUtils.isEmpty(player.getPictureUrl())) {
            Picasso.with(holder.imgPicture.getContext())
                    .load(player.getPictureUrl())
                    .into(holder.imgPicture);

        }
        setItemAppearence(holder);
        return convertView;
    }

    private void setItemAppearence(ViewHolder holder) {
        Player player = mMatchManager.getPlayers().get(holder.position);
        holder.selected.setVisibility(player.isSelected() ? View.VISIBLE : View.INVISIBLE);
        if (null != mOnPlayersStatusChangedListener) {
            mOnPlayersStatusChangedListener.OnPlayersStatusChanged(player);
        }
    }

    /**
     * ViewHolder pattern
     */
    private static class ViewHolder {
        public TextView userName;
        public CircleImageView imgPicture;
        public View selected;
        public int position;
    }

    public static interface OnPlayersStatusChangedListener {
        public void OnPlayersStatusChanged(Player player);
    };
}
