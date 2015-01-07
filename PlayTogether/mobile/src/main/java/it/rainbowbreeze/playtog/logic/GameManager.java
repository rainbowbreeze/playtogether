package it.rainbowbreeze.playtog.logic;

import android.database.Cursor;
import android.support.v4.content.Loader;

import com.squareup.otto.Bus;

import it.rainbowbreeze.playtog.common.ILogFacility;
import it.rainbowbreeze.playtog.data.AppPrefsManager;
import it.rainbowbreeze.playtog.data.PlayerDao;
import it.rainbowbreeze.playtog.domain.Player;

/**
 * Manages a match (gathers players, keeps track of subscriptions etc)
 *
 * Created by alfredomorresi on 02/01/15.
 */
public class GameManager {
    private static final String LOG_TAG = GameManager.class.getSimpleName();

    private final ILogFacility mLogFacility;
    private final Bus mBus;
    private final PlayerDao mPlayerDao;
    private final BackendHelper mBackendHelper;
    private final AppPrefsManager mAppPrefsManager;
    private boolean mStartedSearchForPlayers;

    public GameManager(ILogFacility logFacility, PlayerDao playerDao, AppPrefsManager appPrefsManager, BackendHelper backendHelper, Bus bus) {
        mLogFacility = logFacility;
        mPlayerDao = playerDao;
        mAppPrefsManager = appPrefsManager;
        mBackendHelper = backendHelper;
        mBus = bus;
        cleanGameStateAndData();
    }

    /**
     * Add a new player for the game
     *
     * @param newPlayer
     */
    private void add(Player newPlayer) {
        mPlayerDao.insert(newPlayer);
    }

    /**
     * Returns a {@link android.database.Cursor} Loader for the players that accepted the game
     * @return
     */
    public Loader<Cursor> getPlayersForTheGame() {
        return mPlayerDao.getPlayersForTheGame();
    }

    /**
     * Clean all the game data. Used, generally, when the app starts for the first time or
     * when a new call for player is started
     */
    public void cleanGameStateAndData() {
        mStartedSearchForPlayers = false;
        mPlayerDao.deleteAll();
    }

    /**
     * Checks if a call for players can be launched, given the actual context (if another call has
     * already been started, etc)
     * @return
     */
    public boolean canSearchForPlayers() {
        return !mStartedSearchForPlayers;
    }

    /**
     * Checks if a game can be started, given the selected players and other data
     * @return
     */
    public boolean canStartAGame() {
        return mPlayerDao.countSelectedPlayers() == 4;
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
     * Starts the search for players
     */
    public void startSearchingForPlayer() {
        cleanGameStateAndData();  // Just to be sure!

        mStartedSearchForPlayers = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                mBackendHelper.searchForPlayers();

                // Add the current player as first player of the list
                add(mAppPrefsManager.getCurrentPlayer().setSelected(true));

                for (int i=0; i<5; i++) {
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switch (i) {
                        case 0:
                            add(new Player().setName("Stefano Manca").setSelected(false).setPictureUrl("https://lh6.googleusercontent.com/-nmitA-hkSEQ/Uq2f8lNFZ6I/AAAAAAAAIUo/81UQRcbPRnM/s788-no/54ef821b-b16c-44d6-afac-61a835a7a2bf"));
                            break;
                        case 1:
                            add(new Player().setName("Alessandra Pugin").setSelected(false).setPictureUrl("https://lh3.googleusercontent.com/-Fymin8OPcGA/UrbfjUs7GEI/AAAAAAAAHxc/8FTjOYsnI9c/w480-h480/41356_484793799347_4458943_n.jpg"));
                            break;
                        case 2:
                            add(new Player().setName("Fabio Ercolani").setSelected(false).setPictureUrl("https://lh6.googleusercontent.com/-tIbufLRCQes/Tj-ceTA9C1I/AAAAAAAAG2Q/SZIq-bZCRv0/w790-h788-no/Nina%2B021.jpg"));
                            break;
                        case 3:
                            add(new Player().setName("Valentina Frassi").setSelected(false).setPictureUrl("http://lorempixel.com/400/400"));
                            break;
                        default:
                            add(new Player().setName("Alessandro Antiga" + i).setSelected(false).setPictureUrl("https://lh6.googleusercontent.com/-txy6s8_3HSU/UjsrZJT6jkI/AAAAAAAAHuw/xdSVu4KCH8U/s512-no/IMG_20130919_130416.jpg"));
                            break;
                    }
                    //mLogFacility.v(LOG_TAG, "Posting new player " + i);
                    //mBus.post(new PlayersUpdateEvent());
                }

            }
        }).start();
        
    }

    /**
     * Starts the game with the selected players
     */
    public void startTheGame() {
        mStartedSearchForPlayers = false;
        mPlayerDao.deleteAll();

        //TODO Sends notification to selected and not selected players


        //TODO Notifies the backend about the start of the game
    }

}
