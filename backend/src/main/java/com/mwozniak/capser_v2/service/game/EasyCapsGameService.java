package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.EasyCapsRepository;
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
public class EasyCapsGameService extends SoloGameService {

    private final EasyCapsRepository easyCapsRepository;

    @Override
    public AcceptanceRequestType getAcceptanceRequestType() {
        return AcceptanceRequestType.EASY;
    }


    public EasyCapsGameService(AcceptanceRequestRepository acceptanceRequestRepository, AchievementService achievementService, EmailService emailService, UserService userService, NotificationService notificationService, EasyCapsRepository easyCapsRepository) {
        super(acceptanceRequestRepository, achievementService, userService, emailService, notificationService);
        this.easyCapsRepository = easyCapsRepository;
    }

    @Override
    protected void doProcessAchievements(User user, AbstractGame abstractGame) {
        achievementService.processEasyAchievements(user, abstractGame);
    }

    @Override
    public AbstractGame saveGame(AbstractGame abstractGame) {
        return easyCapsRepository.save((EasyCapsGame) abstractGame);
    }

    @Override
    public void removeGame(AbstractGame abstractGame) {
        easyCapsRepository.delete((EasyCapsGame) abstractGame);
    }


    @Override
    public EasyCapsGame findGame(UUID uuid) throws CapserException {
        Optional<EasyCapsGame> singlesGameOptional = easyCapsRepository.findEasyCapsGameById(uuid);
        if (singlesGameOptional.isPresent()) {
            return singlesGameOptional.get();
        } else {
            throw new GameNotFoundException("This easy caps game doesn't exist");
        }
    }

    @Override
    public List<AbstractGame> listGames() {
        return (List<AbstractGame>) (List<?>) easyCapsRepository.findAll();
    }

    @Override
    public Page<AbstractGame> listGames(Pageable pageable) {
        return (Page<AbstractGame>) (Page<?>) easyCapsRepository.findAll(pageable);
    }

    @Override
    protected Page<AbstractGame> getAcceptedGames(Pageable pageable) {
        return (Page<AbstractGame>) (Page<?>) easyCapsRepository.findEasyCapsGamesByAcceptedTrue(pageable);
    }

    @Override
    protected Page<AbstractGame> getPlayerAcceptedGames(Pageable pageable, UUID player) {
        return (Page<AbstractGame>) (Page<?>) easyCapsRepository.findEasyCapsGamesByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(pageable, player, player);
    }

    @Override
    protected Page<? extends AbstractGame> getGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2) {
        return easyCapsRepository.findEasyGamesWithPlayerAndOpponent(PageRequest.of(0, 10, Sort.by("time").descending()),
                player1, player2);
    }


    @Override
    public GameType getGameType() {
        return GameType.EASY_CAPS;
    }

}
