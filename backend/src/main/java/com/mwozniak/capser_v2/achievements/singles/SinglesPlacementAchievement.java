package com.mwozniak.capser_v2.achievements.singles;

import com.mwozniak.capser_v2.achievements.PlacementAchievement;
import com.mwozniak.capser_v2.achievements.SinglesAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;

@SinglesAchievement
public class SinglesPlacementAchievement extends PlacementAchievement {

    @Override
    public boolean checkConditions(User user, AbstractGame abstractGame) {
        return checkPlacementConditions(user.getUserSinglesStats());
    }

    @Override
    public Achievement getAchievement() {
        return Achievement.PLACE_IN_SINGLES;
    }
}
