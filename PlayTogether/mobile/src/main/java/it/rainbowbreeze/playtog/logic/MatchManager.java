package it.rainbowbreeze.playtog.logic;

import com.squareup.otto.Bus;

import java.util.ArrayList;
import java.util.List;

import it.rainbowbreeze.playtog.common.ILogFacility;
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
    private final List<Player> mPlayers;
    private boolean mStartedSearchForPlayers;

    public MatchManager(ILogFacility logFacility, Bus bus) {
        mLogFacility = logFacility;
        mBus = bus;
        mPlayers = new ArrayList<Player>();
        mStartedSearchForPlayers = false;

        add(new Player().setName("Alfredo - player 0").setSelected(false).setPictureUrl("http://lorempixel.com/400/400"));
    }

    public void add(Player newPlayer) {
        mPlayers.add(newPlayer);
    }

    public void removeAllPlayer() {
        mPlayers.clear();
    }

    public List<Player> getPlayers() {
        return mPlayers;
    }


    public boolean isCallForPlayersEnabled() {
        return true;
    }

    public boolean canStartAGame() {
        int selected = 0;
        for (Player player : mPlayers) {
            if (player.isSelected()) selected ++;
        }
        return selected > 2;
    }

    public void togglePlayerSelection(int playerPosition) {
        if (playerPosition > mPlayers.size()) {
            mLogFacility.e(LOG_TAG, "Index out of bounds: required " + playerPosition + ", size: " + mPlayers.size());
            return;
        }
        mPlayers.get(playerPosition).toggleSelected();
    }

    public boolean canSearchForPlayers() {
        return !mStartedSearchForPlayers;
    }

    public void startSearchingForPlayer() {
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

        // Sends notification to selected players

        // Change views
    }
}
