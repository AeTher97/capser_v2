package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.achievements.AchievementProcessor;
import com.mwozniak.capser_v2.achievements.DoublesAchievement;
import com.mwozniak.capser_v2.achievements.EasyAchievement;
import com.mwozniak.capser_v2.achievements.SinglesAchievement;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.models.database.AchievementEntity;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Service
public class AchievementService {

    private final List<AchievementProcessor> easyAchievementProcessors;
    private final List<AchievementProcessor> singlesAchievementProcessors;
    private final List<AchievementProcessor> doublesAchievementsProcessors;

    private final NotificationService notificationService;

    public AchievementService(ApplicationContext applicationContext, UserService userService, NotificationService notificationService) {
        easyAchievementProcessors = applicationContext.getBeansWithAnnotation(EasyAchievement.class)
                .values().stream().map(AchievementProcessor.class::cast).collect(Collectors.toList());

        singlesAchievementProcessors = applicationContext.getBeansWithAnnotation(SinglesAchievement.class)
                .values().stream().map(AchievementProcessor.class::cast).collect(Collectors.toList());

        doublesAchievementsProcessors = applicationContext.getBeansWithAnnotation(DoublesAchievement.class)
                .values().stream().map(AchievementProcessor.class::cast).collect(Collectors.toList());


        this.notificationService = notificationService;
    }

    public void processEasyAchievements(User user, AbstractGame easyCapsGame) {
        processAchievementType(user, easyCapsGame, easyAchievementProcessors);
    }

    public void processSinglesAchievements(User user, AbstractGame singlesGame) {
        processAchievementType(user, singlesGame, singlesAchievementProcessors);
    }


    public void processDoublesAchievements(User user, AbstractGame doublesGame) {
        processAchievementType(user, doublesGame, doublesAchievementsProcessors);
    }


    private void processAchievementType(User user, AbstractGame abstractGame, List<AchievementProcessor> achievementProcessors) {
        achievementProcessors.forEach(achievementProcessor -> {
            if (achievementProcessor.checkConditions(user, abstractGame)
                    && !checkIfAchievementUnlocked(user.getAchievementEntities(), achievementProcessor.getAchievement())) {
                notificationService.notifyAboutAchievement(user.getId(), achievementProcessor.getAchievement().getName());
                user.getAchievementEntities().add(achievementProcessor.createEntity(user.getId()));
            }
        });
    }


    private boolean checkIfAchievementUnlocked(List<AchievementEntity> achievementEntities, Achievement achievement) {
        AtomicBoolean contains = new AtomicBoolean(false);
        achievementEntities.forEach(achievementEntity -> {
            if (achievementEntity.getAchievement().equals(achievement)) {
                contains.set(true);
            }
        });
        return contains.get();
    }
}
