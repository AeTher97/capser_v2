package com.mwozniak.capser_v2.achievements.easy;

import com.mwozniak.capser_v2.achievements.EasyAchievement;
import com.mwozniak.capser_v2.achievements.PlacementAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;


@EasyAchievement
public class EasyPlacementAchievement extends PlacementAchievement {

    @Override
    public boolean checkConditions(User user, AbstractGame abstractGame) {
        return checkPlacementConditions(user.getUserEasyStats());
    }

    @Override
    public Achievement getAchievement() {
        return Achievement.PLACE_IN_EASY;
    }
}
