package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.service.EmailService;
import com.mwozniak.capser_v2.service.NotificationService;
import com.mwozniak.capser_v2.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public abstract class SoloGameService extends AbstractGameService {


    public SoloGameService(AcceptanceRequestRepository acceptanceRequestRepository, UserService userService, EmailService emailService, NotificationService notificationService) {
        super(acceptanceRequestRepository, userService, emailService, notificationService);
    }

    @Override
    public Page<AbstractGame> listAcceptedGames(Pageable pageable) {
        Page<AbstractGame> games = getAcceptedGames(pageable);
        games = mapGamesWithPlayerNames(games);

        return games;
    }

    @Override
    public Page<AbstractGame> listPlayerAcceptedGames(Pageable pageable, UUID player) {
        Page<AbstractGame> games = getPlayerAcceptedGames(pageable, player);
        games = mapGamesWithPlayerNames(games);

        return games;
    }

    public Page<? extends AbstractGame> listGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2) {
        Page<? extends AbstractGame> games = getGamesWithPlayerAndOpponent(pageable, player1, player2);
        games = mapGamesWithPlayerNames(games);

        return games;
    }


    private Page<AbstractGame> mapGamesWithPlayerNames(Page<? extends AbstractGame> games) {
        return games.map(game -> {
            AbstractSinglesGame singlesGame = (AbstractSinglesGame) game;
            try {
                String user1Name = userService.getUser(singlesGame.getPlayer1()).getUsername();
                String user2Name = userService.getUser(singlesGame.getPlayer2()).getUsername();

                singlesGame.setTeam1Name(user1Name);
                singlesGame.setTeam2Name(user2Name);

                return singlesGame;
            } catch (UserNotFoundException e) {
                e.printStackTrace();
                return game;
            }
        });
    }


    protected abstract Page<? extends AbstractGame> getGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2);

    protected abstract Page<AbstractGame> getAcceptedGames(Pageable pageable);

    protected abstract Page<AbstractGame> getPlayerAcceptedGames(Pageable pageable, UUID player);
}