package com.mwozniak.capser_v2.achievements;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.dto.CreateUserDto;
import com.mwozniak.capser_v2.models.dto.PlayerStatsDto;
import com.mwozniak.capser_v2.models.dto.SoloGameDto;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.service.game.EasyCapsGameService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestPropertySource(properties = {"CRON_EXPRESSION = 0 10 * * * *"})
class AchievementsTest {

    @Autowired
    private UserService userService;

    @Autowired
    private EasyCapsGameService easyCapsGameService;

    private UUID user1Id;
    private UUID user2Id;


    @BeforeEach
    public void setUpDatabase() throws NoSuchAlgorithmException {
        User user1 = userService.createUser(CreateUserDto.builder()
                .email("test@gmail.com")
                .password("123test")
                .repeatPassword("123test")
                .username("test1")
                .build());
        user1Id = user1.getId();
        User user2 = userService.createUser(CreateUserDto.builder()
                .email("test2@gmail.com")
                .password("123test")
                .repeatPassword("123test")
                .username("test2")
                .build());
        user2Id = user2.getId();
    }

    @Test
    void testFirstGameAchievementFromEasyGame() {
        EasyCapsGame easyCapsGame = new EasyCapsGame();
        SoloGameDto soloGameDto = new SoloGameDto();
        soloGameDto.setGameMode(GameMode.SUDDEN_DEATH);
        soloGameDto.setPlayer1Stats(PlayerStatsDto.builder()
                .playerId(user1Id)
                .score(11)
                .sinks(15)
                .build());

        soloGameDto.setPlayer2Stats(PlayerStatsDto.builder()
                .playerId(user2Id)
                .score(10)
                .sinks(15)
                .build());


        easyCapsGame.fillCommonProperties(soloGameDto);

        easyCapsGameService.postGameWithoutAcceptance(easyCapsGame);
    }
}
