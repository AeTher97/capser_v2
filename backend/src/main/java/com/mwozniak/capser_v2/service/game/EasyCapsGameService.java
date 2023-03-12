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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EasyCapsGameService extends SoloGameService<EasyCapsGame> {

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
    protected void doProcessAchievements(User user, EasyCapsGame game) {
        achievementService.processEasyAchievements(user, game);
    }

    @Override
    public EasyCapsGame saveGame(EasyCapsGame game) {
        return easyCapsRepository.save(game);
    }

    @Override
    public void removeGame(EasyCapsGame game) {
        easyCapsRepository.delete(game);
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
    public List<EasyCapsGame> listGames() {
        return easyCapsRepository.findAll();
    }

    @Override
    public Page<EasyCapsGame> listGames(Pageable pageable) {
        return easyCapsRepository.findAll(pageable);
    }

    @Override
    protected Page<EasyCapsGame> getAcceptedGames(Pageable pageable) {
        return easyCapsRepository.findEasyCapsGamesByAcceptedTrue(pageable);
    }

    @Override
    protected Page<EasyCapsGame> getPlayerAcceptedGames(Pageable pageable, UUID player) {
        return  easyCapsRepository.findEasyCapsGamesByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(pageable, player, player);
    }

    @Override
    protected Page<EasyCapsGame> getGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2) {
        return easyCapsRepository.findEasyGamesWithPlayerAndOpponent(pageable,
                player1, player2);
    }


    @Override
    public GameType getGameType() {
        return GameType.EASY_CAPS;
    }

}
