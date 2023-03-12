package com.mwozniak.capser_v2.achievements;

import com.mwozniak.capser_v2.achievements.general.FirstNakedLapAchievement;
import com.mwozniak.capser_v2.achievements.general.FirstWinAchievement;
import com.mwozniak.capser_v2.achievements.general.PlayFirstCapsGameAchievement;
import com.mwozniak.capser_v2.achievements.general.rebuttals.RebutOnLastChanceAchievement;
import com.mwozniak.capser_v2.achievements.general.rebuttals.RebuttalsInARowAchievement5;
import com.mwozniak.capser_v2.enums.Achievement;
import com.mwozniak.capser_v2.enums.GameEvent;
import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.GameEventEntity;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import com.mwozniak.capser_v2.models.dto.CreateUserDto;
import com.mwozniak.capser_v2.models.dto.PlayerStatsDto;
import com.mwozniak.capser_v2.models.dto.SoloGameDto;
import com.mwozniak.capser_v2.service.AchievementService;
import com.mwozniak.capser_v2.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationContext;

import java.security.NoSuchAlgorithmException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


class AchievementsTest {


    @Mock
    private ApplicationContext applicationContext;

    @Mock
    private NotificationService notificationService;

    private AchievementService achievementService;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        user1 = User.createUserFromDto(CreateUserDto.builder()
                .username("test1")
                .email("test@gmail.com")
                .build(), "12321");
        user1.setId(UUID.randomUUID());

        user2 = User.createUserFromDto(CreateUserDto.builder()
                .username("test2")
                .email("test2@gmail.com")
                .build(), "12321");
        user2.setId(UUID.randomUUID());

        MockitoAnnotations.initMocks(this);
        achievementService = new AchievementService(applicationContext, notificationService);
    }

    private void fillSoloGameData(int user1Points, int user2Points, int user1Sinks, int user2Sinks,
                                  AbstractGame abstractGame) {
        fillSoloGameData(user1Points, user2Points, user1Sinks, user2Sinks, abstractGame, new ArrayList<>());
    }

    private void fillSoloGameData(int user1Points, int user2Points, int user1Sinks, int user2Sinks,
                                  AbstractGame abstractGame, List<GameEventEntity> gameEvents) {
        SoloGameDto soloGameDto = new SoloGameDto();
        soloGameDto.setGameMode(GameMode.SUDDEN_DEATH);
        soloGameDto.setGameEvents(gameEvents);
        soloGameDto.setPlayer1Stats(PlayerStatsDto.builder()
                .playerId(user1.getId())
                .score(user1Points)
                .sinks(user2Points)
                .build());

        soloGameDto.setPlayer2Stats(PlayerStatsDto.builder()
                .playerId(user2.getId())
                .score(user1Sinks)
                .sinks(user2Sinks)
                .build());

        abstractGame.fillCommonProperties(soloGameDto);
        abstractGame.calculateStatsOfAllPlayers();

        abstractGame.calculatePlayerStats(user1);
        abstractGame.calculatePlayerStats(user2);

    }


    private Map<String, Object> getObjectMap(Object... achievementProcessors) {
        return new HashMap<String, Object>() {{
            Arrays.stream(achievementProcessors).forEach(processor ->
                    put("processor" + processor.getClass().toString(), processor));
        }};
    }

    @Test
    void testFirstGameAchievementFromEasyGame() {
        EasyCapsGame easyCapsGame = new EasyCapsGame();
        fillSoloGameData(11, 15, 5, 7, easyCapsGame);

        when(applicationContext.getBeansWithAnnotation(EasyAchievement.class))
                .thenReturn(getObjectMap(new PlayFirstCapsGameAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processEasyAchievements(user1, easyCapsGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.PLAY_FIRST_GAME, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testFirstGameAchievementFromSinglesGame() {
        SoloGame singlesGame = new SoloGame();
        fillSoloGameData(11, 15, 5, 7, singlesGame);

        when(applicationContext.getBeansWithAnnotation(SinglesAchievement.class))
                .thenReturn(getObjectMap(new PlayFirstCapsGameAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processSinglesAchievements(user1, singlesGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.PLAY_FIRST_GAME, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testFirstNakedLapAchievementFromEasyGame() {
        EasyCapsGame easyCapsGame = new EasyCapsGame();
        fillSoloGameData(0, 11, 5, 7, easyCapsGame);

        when(applicationContext.getBeansWithAnnotation(EasyAchievement.class))
                .thenReturn(getObjectMap(new FirstNakedLapAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processEasyAchievements(user1, easyCapsGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.FIRST_NAKED_LAP, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testFirstNakedLapAchievementFromSinglesGame() {
        SoloGame singlesGame = new SoloGame();
        fillSoloGameData(0, 11, 5, 7, singlesGame);

        when(applicationContext.getBeansWithAnnotation(SinglesAchievement.class))
                .thenReturn(getObjectMap(new FirstNakedLapAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processSinglesAchievements(user1, singlesGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.FIRST_NAKED_LAP, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testFirstWinAchievementFromEasyGame() {
        EasyCapsGame easyCapsGame = new EasyCapsGame();
        fillSoloGameData(11, 5, 5, 7, easyCapsGame);

        when(applicationContext.getBeansWithAnnotation(EasyAchievement.class))
                .thenReturn(getObjectMap(new FirstWinAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processEasyAchievements(user1, easyCapsGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.FIRST_WIN, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testFirstWinAchievementFromSinglesGame() {
        SoloGame singlesGame = new SoloGame();
        fillSoloGameData(11, 3, 5, 7, singlesGame);

        when(applicationContext.getBeansWithAnnotation(SinglesAchievement.class))
                .thenReturn(getObjectMap(new FirstWinAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processSinglesAchievements(user1, singlesGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.FIRST_WIN, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testRebuttalStreakFromEasyGame() {
        EasyCapsGame easyCapsGame = new EasyCapsGame();
        fillSoloGameData(11, 5, 5, 7, easyCapsGame,
                Arrays.asList(
                        new GameEventEntity(null, GameEvent.SINK, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId())
                ));

        when(applicationContext.getBeansWithAnnotation(EasyAchievement.class))
                .thenReturn(getObjectMap(new RebuttalsInARowAchievement5()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processEasyAchievements(user1, easyCapsGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.REBUTTALS_IN_A_ROW_5, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testRebuttalStreakFromSinglesGame() {
        SoloGame singlesGame = new SoloGame();
        fillSoloGameData(11, 3, 5, 7, singlesGame,
                Arrays.asList(
                        new GameEventEntity(null, GameEvent.SINK, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId())
                ));

        when(applicationContext.getBeansWithAnnotation(SinglesAchievement.class))
                .thenReturn(getObjectMap(new RebuttalsInARowAchievement5()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processSinglesAchievements(user1, singlesGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.REBUTTALS_IN_A_ROW_5, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testRebuttalOnLastChanceFromEasyGame() {
        EasyCapsGame easyCapsGame = new EasyCapsGame();
        fillSoloGameData(11, 5, 5, 7, easyCapsGame,
                Arrays.asList(
                        new GameEventEntity(null, GameEvent.SINK, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.SINK, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId())
                ));

        when(applicationContext.getBeansWithAnnotation(EasyAchievement.class))
                .thenReturn(getObjectMap(new RebutOnLastChanceAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processEasyAchievements(user1, easyCapsGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.REBUT_ON_LAST_CHANCE, user1.getAchievementEntities().get(0).getAchievement());
    }

    @Test
    void testRebuttalOnLastChanceFromSinglesGame() {
        SoloGame singlesGame = new SoloGame();
        fillSoloGameData(11, 3, 5, 7, singlesGame,
                Arrays.asList(
                        new GameEventEntity(null, GameEvent.SINK, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.POINT, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.SINK, new Date(), user2.getId()),
                        new GameEventEntity(null, GameEvent.REBUTTAL, new Date(), user1.getId())
                ));

        when(applicationContext.getBeansWithAnnotation(SinglesAchievement.class))
                .thenReturn(getObjectMap(new RebutOnLastChanceAchievement()));
        achievementService.initializeProcessors(applicationContext);

        achievementService.processSinglesAchievements(user1, singlesGame);

        assertEquals(1, user1.getAchievementEntities().size());
        assertEquals(Achievement.REBUT_ON_LAST_CHANCE, user1.getAchievementEntities().get(0).getAchievement());
    }


}
