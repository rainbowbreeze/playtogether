package it.rainbowbreeze.playtog.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class StartGameFragment extends Fragment{
    private static final String LOG_TAG = StartGameFragment.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    private ListView mListView;
    private PlayersAdapter mPlayersAdapter;
    private Button mBtnSearchForPlayers;
    private Button mBtnConfirmGame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fra_startgame, container, false);

        mListView = (ListView) rootView.findViewById(R.id.startgame_lstPlayers);
        mPlayersAdapter = new PlayersAdapter(getActivity());
        mListView.setAdapter(mPlayersAdapter);
        mListView.setItemsCanFocus(false);

        mPlayersAdapter.add("Alfredo - player 1");
        mPlayersAdapter.add("Carla - player 2");
        mPlayersAdapter.add("Marco - player 3");
        mPlayersAdapter.add("Veronica - player 4");

        mBtnSearchForPlayers = (Button) rootView.findViewById(R.id.startgame_btnSearchForPlayers);
        mBtnSearchForPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mBtnConfirmGame = (Button) rootView.findViewById(R.id.startgame_btnConfirmGame);
        mBtnConfirmGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        setButtonsState();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MyApp) activity.getApplicationContext()).inject(this);
    }

    private void setButtonsState() {
        mBtnSearchForPlayers.setEnabled(true);
        mBtnConfirmGame.setEnabled(false);
    }
}
