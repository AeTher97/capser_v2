package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.SinglesGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.UsersRepository;
import com.mwozniak.capser_v2.utils.EloRating;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public abstract class AbstractGameService implements GameService {

    private final AcceptanceRequestRepository acceptanceRequestRepository;
    private final UserService userService;

    private final NotificationService notificationService;


    public AbstractGameService(AcceptanceRequestRepository acceptanceRequestRepository,
                               UserService userService,
                               NotificationService notificationService) {
        this.acceptanceRequestRepository = acceptanceRequestRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Transactional
    @Override
    public void queueGame(AbstractGame abstractGame) throws UserNotFoundException {
        SinglesGame singlesGame = (SinglesGame) abstractGame;
        User user1 = userService.getUser(singlesGame.getPlayer1());
        User user2 = userService.getUser(singlesGame.getPlayer2());


        SinglesGame saved = (SinglesGame)saveGame(singlesGame);

        AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                AcceptanceRequestType.PASSIVE,
                singlesGame.getPlayer1(), saved.getId());
        AcceptanceRequest activeAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                AcceptanceRequestType.SINGLE,
                singlesGame.getPlayer2(), saved.getId());

        acceptanceRequestRepository.save(posterAcceptanceRequest);
        acceptanceRequestRepository.save(activeAcceptanceRequest);

        notificationService.notify(posterAcceptanceRequest, user2.getUsername());
        notificationService.notify(posterAcceptanceRequest, user1.getUsername());
    }



    @Transactional
    @Override
    public void acceptGame(UUID gameId) throws CapserException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        if (acceptanceRequestList.isEmpty()) {
            throw new GameNotFoundException("Cannot find game to accept with this id");
        }

        AcceptanceRequest request = acceptanceRequestList.stream().findFirst().get();

        SinglesGame singlesGame = (SinglesGame)findGame(request.getGameToAccept());

        User user1 = userService.getUser(singlesGame.getPlayer1());
        User user2 = userService.getUser(singlesGame.getPlayer2());



        singlesGame.setAccepted(true);

        boolean d;
        d = singlesGame.getWinner().equals(user1.getId());


        List<User> listToPass = Arrays.asList(user1, user2);
        float player1PreviousRating = user1.getUserSinglesStats().getPoints();
        float player2PreviousRating = user2.getUserSinglesStats().getPoints();

        EloRating.calculate(listToPass, 30, d);

        float player1PointsChange = user1.getUserSinglesStats().getPoints() - player1PreviousRating;
        float player2PointsChange = user2.getUserSinglesStats().getPoints() - player2PreviousRating;

        singlesGame.getPlayer1Stats().setPointsChange(player1PointsChange);
        singlesGame.getPlayer2Stats().setPointsChange(player2PointsChange);

        userService.updatePlayerSinglesStats(user1, player1PointsChange);
        userService.updatePlayerSinglesStats(user2,player2PointsChange);


        acceptanceRequestList.forEach(acceptanceRequestRepository::delete);

        saveGame(singlesGame);


    }

    public abstract AbstractGame saveGame(AbstractGame abstractGame);

}
