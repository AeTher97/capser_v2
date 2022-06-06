package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.SinglesRepository;
import com.mwozniak.capser_v2.service.AchievementService;
import com.mwozniak.capser_v2.service.EmailService;
import com.mwozniak.capser_v2.service.NotificationService;
import com.mwozniak.capser_v2.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SinglesGameService extends SoloGameService {

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
    protected void doProcessAchievements(User user, AbstractGame abstractGame) {
        achievementService.processSinglesAchievements(user, abstractGame);
    }

    @Override
    public AbstractGame saveGame(AbstractGame abstractGame) {
        return singlesRepository.save((SinglesGame) abstractGame);
    }

    @Override
    public void removeGame(AbstractGame abstractGame) {
        singlesRepository.delete((SinglesGame) abstractGame);
    }


    @Override
    public SinglesGame findGame(UUID uuid) throws CapserException {
        Optional<SinglesGame> singlesGameOptional = singlesRepository.findSinglesGameById(uuid);
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
    public List<AbstractGame> listGames() {
        return (List<AbstractGame>)(List<?>)singlesRepository.findAll();
    }

    @Override
    public Page<AbstractGame> listGames(Pageable pageable) {
        return (Page<AbstractGame>) (Page<?>) singlesRepository.findAll(pageable);
    }

    @Override
    protected Page<AbstractGame> getAcceptedGames(Pageable pageable) {
        return (Page<AbstractGame>) (Page<?>) singlesRepository.findSinglesGamesByAcceptedTrue(pageable);
    }

    @Override
    protected Page<AbstractGame> getPlayerAcceptedGames(Pageable pageable, UUID player) {
        return (Page<AbstractGame>) (Page<?>) singlesRepository.findSinglesGamesByAcceptedTrue(pageable);
    }

    @Override
    protected Page<? extends AbstractGame> getGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2) {
        return singlesRepository.findSinglesGamesWithPlayerAndOpponent(PageRequest.of(0, 10, Sort.by("time").descending()),
                player1, player2);
    }


    @Override
    public GameType getGameType() {
        return GameType.SINGLES;
    }
}
