package it.rainbowbreeze.playtog.ui;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.squareup.otto.Bus;

import javax.inject.Inject;

import it.rainbowbreeze.playtog.R;
import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.common.MyApp;
import it.rainbowbreeze.playtog.logic.MatchManager;

/**
 * Created by alfredomorresi on 02/01/15.
 */
public class StartGameFragment
        extends Fragment
        implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String LOG_TAG = StartGameFragment.class.getSimpleName();

    @Inject ILogFacility mLogFacility;
    @Inject MatchManager mMatchManager;
    @Inject Bus mBus;
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
        mPlayersAdapter = new PlayersAdapter(
                getActivity().getApplicationContext(), null, true,
                mLogFacility);
        mListView.setAdapter(mPlayersAdapter);
        //mListView.setItemsCanFocus(false);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //TODO put in background thread
                mMatchManager.togglePlayerSelection(id);
                updateViewsStatus();
            }
        });
        getLoaderManager().initLoader(0, null, this);

        mBtnSearchForPlayers = (Button) rootView.findViewById(R.id.startgame_btnSearchForPlayers);
        mBtnSearchForPlayers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMatchManager.startSearchingForPlayer();
            }
        });
        mBtnConfirmGame = (Button) rootView.findViewById(R.id.startgame_btnConfirmGame);
        mBtnConfirmGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMatchManager.startTheGame();
            }
        });
        updateViewsStatus();

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MyApp) activity.getApplicationContext()).inject(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        mBus.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        mBus.unregister(this);
    }

    private void updateViewsStatus() {
        mBtnSearchForPlayers.setEnabled(mMatchManager.canSearchForPlayers());
        mBtnConfirmGame.setEnabled(mMatchManager.canStartAGame());
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return mMatchManager.getPlayersForTheGame();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mPlayersAdapter.changeCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mPlayersAdapter.changeCursor(null);
    }
}
