package it.rainbowbreeze.playtog.data;

import it.rainbowbreeze.playtog.domain.GameRecord;

/**
 * Created by alfredomorresi on 08/01/15.
 */
public class GameDao extends BaseOfyDao<GameRecord>{

    public GameDao() {
        super(GameRecord.class);
    }
}
