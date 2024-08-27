package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
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
public class UnrankedGameService extends SoloGameService<UnrankedGame> {

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
    protected void doProcessAchievements(User user, UnrankedGame game) {
        //NO UNRANKED ACHIEVEMENTS!
    }

    @Override
    public UnrankedGame saveGame(UnrankedGame game) {
        return unrankedRepository.save(game);
    }

    @Override
    public void removeGame(UnrankedGame abstractGame) {
        unrankedRepository.delete(abstractGame);
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
    public List<UnrankedGame> listGames() {
        return unrankedRepository.findAll();
    }

    @Override
    public Page<UnrankedGame> listGames(Pageable pageable) {
        return unrankedRepository.findAll(pageable);
    }

    @Override
    protected Page<UnrankedGame> getAcceptedGames(Pageable pageable) {
        return unrankedRepository.findUnrankedGameByAcceptedTrue(pageable);
    }

    @Override
    protected Page<UnrankedGame> getPlayerAcceptedGames(Pageable pageable, UUID player) {
        return unrankedRepository.findUnrankedGameByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(pageable, player, player);
    }


    @Override
    protected Page<UnrankedGame> getGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2) {
        return unrankedRepository.findUnrankedGamesWithPlayerAndOpponent(pageable,
                player1, player2);
    }

    @Override
    protected List<UnrankedGame> getGamesWithPlayerAndOpponent(UUID player1, UUID player2) {
        return unrankedRepository.findUnrankedGamesWithPlayerAndOpponent(player1, player2);
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
