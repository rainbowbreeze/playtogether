package it.rainbowbreeze.playtog.logic;

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
    private final List<Player> mPlayers;

    public MatchManager(ILogFacility logFacility) {
        this.mLogFacility = logFacility;
        mPlayers = new ArrayList<Player>();

        add(new Player().setName("Alfredo - player 1").setSelected(false).setPictureUrl("http://lorempixel.com/400/400/sports"));
        add(new Player().setName("Carla - player 2").setSelected(false).setPictureUrl("http://lorempixel.com/400/400/food"));
        add(new Player().setName("Marco - player 3").setSelected(true).setPictureUrl("http://lorempixel.com/400/400"));
        add(new Player().setName("Veronica - player 4").setSelected(false).setPictureUrl("http://lorempixel.com/400/400/nature"));
        add(new Player().setName("Luigi - player 5").setSelected(false));
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

    public void toggleSelection(int playerPosition) {
        if (playerPosition > mPlayers.size()) {
            mLogFacility.e(LOG_TAG, "Index out of bounds: required " + playerPosition + ", size: " + mPlayers.size());
            return;
        }
        mPlayers.get(playerPosition).toggleSelected();
    }

    public boolean canSearchForPlayers() {
        return true;
    }
}
