package com.mwozniak.capser_v2.achievements.general.rebuttals;

import com.mwozniak.capser_v2.achievements.EasyAchievement;
import com.mwozniak.capser_v2.achievements.SinglesAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.User;

import java.util.UUID;

@EasyAchievement
@SinglesAchievement
public class RebuttalsInARowAchievement7 extends RebuttalsInARowAchievement {

    @Override
    public Achievement getAchievement() {
        return Achievement.REBUTTALS_IN_A_ROW_5;
    }

    @Override
    protected int getNumberInARow() {
        return 7;
    }

}
