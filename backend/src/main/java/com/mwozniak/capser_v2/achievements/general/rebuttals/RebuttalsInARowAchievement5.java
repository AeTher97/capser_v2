package com.mwozniak.capser_v2.achievements.general.rebuttals;

import com.mwozniak.capser_v2.achievements.EasyAchievement;
import com.mwozniak.capser_v2.achievements.SinglesAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.models.database.AchievementEntity;

import java.util.UUID;

@EasyAchievement
@SinglesAchievement
public class RebuttalsInARowAchievement5 extends RebuttalsInARowAchievement {

    @Override
    public AchievementEntity createEntity(UUID player) {
        return buildAchievementEntity(player);
    }

    @Override
    public Achievement getAchievement() {
        return Achievement.REBUTTALS_IN_A_ROW_5;
    }

    @Override
    protected int getNumberInARow() {
        return 5;
    }

}
