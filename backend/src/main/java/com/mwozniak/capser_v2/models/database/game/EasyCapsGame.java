package com.mwozniak.capser_v2.models.database.game;

import com.mwozniak.capser_v2.enums.GameType;

public class EasyCapsGame extends AbstractSinglesGame {


    @Override
    public GameType getGameType() {
        return GameType.EASY_CAPS;
    }

    @Override
    public void calculateBeers(GamePlayerStats gamePlayerStats1, GamePlayerStats gamePlayerStats2) {
        gamePlayerStats2.setBeersDowned(1.5f);
    }
}
