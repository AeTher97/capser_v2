package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.UnrankedRepository;
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
public class UnrankedGameService extends SoloGameService {

    private final UnrankedRepository unrankedRepository;

    public UnrankedGameService(AcceptanceRequestRepository acceptanceRequestRepository,
                               AchievementService achievementService,
                               UserService userService,
                               NotificationService notificationService,
                               EmailService emailService,
                               UnrankedRepository unrankedRepository) {
        super(acceptanceRequestRepository, achievementService, userService, emailService, notificationService);
        this.unrankedRepository = unrankedRepository;
    }

    @Override
    protected void doProcessAchievements(User user, AbstractGame abstractGame) {
        //NO UNRANKED ACHIEVEMENTS!
    }

    @Override
    public AbstractGame saveGame(AbstractGame abstractGame) {
        return unrankedRepository.save((UnrankedGame) abstractGame);
    }

    @Override
    public void removeGame(AbstractGame abstractGame) {
        unrankedRepository.delete((UnrankedGame) abstractGame);
    }


    @Override
    public UnrankedGame findGame(UUID uuid) throws CapserException {
        Optional<UnrankedGame> unrankedGameOptional = unrankedRepository.findUnrankedGameById(uuid);
        if (unrankedGameOptional.isPresent()) {
            return unrankedGameOptional.get();
        } else {
            throw new GameNotFoundException("This unranked game doesn't exist");
        }
    }

    @Override
    public List<AbstractGame> listGames() {
        return (List<AbstractGame>) (List<?>) unrankedRepository.findAll();
    }

    @Override
    public Page<AbstractGame> listGames(Pageable pageable) {
        return (Page<AbstractGame>) (Page<?>) unrankedRepository.findAll(pageable);
    }

    @Override
    protected Page<AbstractGame> getAcceptedGames(Pageable pageable) {
        return (Page<AbstractGame>) (Page<?>) unrankedRepository.findUnrankedGameByAcceptedTrue(pageable);
    }

    @Override
    protected Page<AbstractGame> getPlayerAcceptedGames(Pageable pageable, UUID player) {
        return (Page<AbstractGame>) (Page<?>) unrankedRepository.findUnrankedGameByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(pageable, player, player);
    }


    @Override
    protected Page<? extends AbstractGame> getGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2) {
        return unrankedRepository.findUnrankedGamesWithPlayerAndOpponent(pageable,
                player1, player2);
    }

    @Override
    public AcceptanceRequestType getAcceptanceRequestType() {
        return AcceptanceRequestType.UNRANKED;
    }

    @Override
    public GameType getGameType() {
        return GameType.UNRANKED;
    }
}
