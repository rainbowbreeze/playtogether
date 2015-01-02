package it.rainbowbreeze.playtog.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import it.rainbowbreeze.libs.ui.widget.CheckableRelativeLayout;
import it.rainbowbreeze.playtog.R;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class PlayersAdapter extends ArrayAdapter<String> {
    public PlayersAdapter(Context context) {
        super(context, R.layout.vw_player_item);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.vw_player_item, parent, false);
            holder = new ViewHolder();
            holder.userName = (TextView) convertView.findViewById(R.id.playeritem_lblPlayerName);
            holder.layout = (CheckableRelativeLayout) convertView.findViewById(R.id.playeritem_layContainer);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.userName.setText(getItem(position));
        // Restore the checked state properly
        //final ListView lv = (ListView) parent;
        //holder.layout.setChecked(lv.isItemChecked(position));
        return convertView;
    }

    /**
     * ViewHolder pattern
     */
    private static class ViewHolder {
        public TextView userName;
        public CheckableRelativeLayout layout;
        int position;
    }
}
