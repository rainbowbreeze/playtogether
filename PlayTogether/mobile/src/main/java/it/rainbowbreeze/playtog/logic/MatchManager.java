package it.rainbowbreeze.playtog.logic;

import android.database.Cursor;
import android.support.v4.content.Loader;

import com.squareup.otto.Bus;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.data.PlayerDao;
import it.rainbowbreeze.playtog.domain.Player;

/**
 * Manages a match (gathers players, keeps track of subscriptions etc)
 *
 * Created by alfredomorresi on 02/01/15.
 */
public class MatchManager {
    private static final String LOG_TAG = MatchManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final Bus mBus;
    private final PlayerDao mPlayerDao;
    private boolean mStartedSearchForPlayers;

    public MatchManager(ILogFacility logFacility, PlayerDao playerDao, Bus bus) {
        mLogFacility = logFacility;
        mPlayerDao = playerDao;
        mBus = bus;
        mStartedSearchForPlayers = false;

        add(new Player().setName("Alfredo - player 0").setSelected(false).setPictureUrl("http://lorempixel.com/400/400"));
    }

    public void add(Player newPlayer) {
        mPlayerDao.insert(newPlayer);
    }

    public boolean isCallForPlayersEnabled() {
        return true;
    }

    public boolean canStartAGame() {
        return mPlayerDao.countSelectedPlayers() > 2;
    }

    /**
     * Toggles the selected state of a player, and do the math to verify if the game can be
     * launched
     *
     * @param playerId
     */
    public void togglePlayerSelection(long playerId) {
        int result = mPlayerDao.toggleSelectionById(playerId);
        if (result > 0) {
            mLogFacility.v(LOG_TAG, "Toggled selection status for player id " + playerId);
        } else {
            mLogFacility.e(LOG_TAG, "Strange, cannot toggle selection status for player id " + playerId);
        }
    }

    /**
     * Returns a {@link android.database.Cursor} Loader for the players that accepted the game
     * @return
     */
    public Loader<Cursor> getPlayersForTheGame() {
        return mPlayerDao.getPlayersForTheGame();
    }

    public boolean canSearchForPlayers() {
        return !mStartedSearchForPlayers;
    }

    /**
     * Starts the search for players
     */
    public void startSearchingForPlayer() {
        mPlayerDao.removeAll();
        //TODO: Add the current player in the list

        //TODO: launch the backend command for starting the search

        mStartedSearchForPlayers = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                int i=0;
                while (i<5) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switch (i) {
                        case 0:
                            add(new Player().setName("Alfredo - player 1").setSelected(false).setPictureUrl("http://lorempixel.com/400/400/sports"));
                            break;
                        case 1:
                            add(new Player().setName("Carla - player 2").setSelected(false).setPictureUrl("http://lorempixel.com/400/400/food"));
                            break;
                        case 2:
                            add(new Player().setName("Marco - player 3").setSelected(true).setPictureUrl("http://lorempixel.com/400/400"));
                            break;
                        case 3:
                            add(new Player().setName("Veronica - player 4").setSelected(false).setPictureUrl("http://lorempixel.com/400/400/nature"));
                            break;
                        default:
                            add(new Player().setName("Luigi - player " + i).setSelected(true));
                            break;
                    }

                    i++;
                    mLogFacility.v(LOG_TAG, "Posting new player " + i);
                    mBus.post(new PlayersUpdateEvent());
                }

            }
        }).start();
        
    }

    public void startTheGame() {
        mStartedSearchForPlayers = false;

        //TODO Sends notification to selected players


        //TODO Notifies the backend about the start of the game
    }
}
