package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.SinglesRepository;
import com.mwozniak.capser_v2.service.AchievementService;
import com.mwozniak.capser_v2.service.EmailService;
import com.mwozniak.capser_v2.service.NotificationService;
import com.mwozniak.capser_v2.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SinglesGameService extends SoloGameService<SoloGame> {

    private final SinglesRepository singlesRepository;

    public SinglesGameService(SinglesRepository singlesRepository,
                              AcceptanceRequestRepository acceptanceRequestRepository,
                              AchievementService achievementService,
                              UserService userService,
                              EmailService emailService,
                              NotificationService notificationService) {
        super(acceptanceRequestRepository, achievementService, userService, emailService, notificationService);
        this.singlesRepository = singlesRepository;
    }



    @Override
    protected void doProcessAchievements(User user, SoloGame game) {
        achievementService.processSinglesAchievements(user, game);
    }

    @Override
    public SoloGame saveGame(SoloGame game) {
        return singlesRepository.save(game);
    }

    @Override
    public void removeGame(SoloGame game) {
        singlesRepository.delete(game);
    }


    @Override
    public SoloGame findGame(UUID uuid) throws CapserException {
        Optional<SoloGame> singlesGameOptional = singlesRepository.findSinglesGameById(uuid);
        if (singlesGameOptional.isPresent()) {
            return singlesGameOptional.get();
        } else {
            throw new GameNotFoundException("This singles game doesn't exist");
        }
    }



    @Override
    public AcceptanceRequestType getAcceptanceRequestType() {
        return AcceptanceRequestType.SINGLE;
    }
    @Override
    public List<SoloGame> listGames() {
        return singlesRepository.findAll();
    }

    @Override
    public Page<SoloGame> listGames(Pageable pageable) {
        return singlesRepository.findAll(pageable);
    }

    @Override
    protected Page<SoloGame> getAcceptedGames(Pageable pageable) {
        return singlesRepository.findSinglesGamesByAcceptedTrue(pageable);
    }

    @Override
    protected Page<SoloGame> getPlayerAcceptedGames(Pageable pageable, UUID player) {
        return singlesRepository.findSinglesGamesByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(pageable, player, player);
    }

    @Override
    protected Page<SoloGame> getGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2) {
        return singlesRepository.findSinglesGamesWithPlayerAndOpponent(pageable,
                player1, player2);
    }

    @Override
    public GameType getGameType() {
        return GameType.SINGLES;
    }
}
