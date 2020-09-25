package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.NotificationType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.Notification;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import com.mwozniak.capser_v2.utils.EloRating;

import javax.transaction.Transactional;
import java.util.*;

public abstract class AbstractGameService implements GameService {

    protected final AcceptanceRequestRepository acceptanceRequestRepository;
    protected final UserService userService;

    protected final NotificationService notificationService;


    public AbstractGameService(AcceptanceRequestRepository acceptanceRequestRepository,
                               UserService userService,
                               NotificationService notificationService) {
        this.acceptanceRequestRepository = acceptanceRequestRepository;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @Transactional
    @Override
    public void queueGame(AbstractGame abstractGame) throws CapserException {
        AbstractGame saved = saveGame(abstractGame);
        addAcceptanceAndNotify(saved);
    }

    protected void addAcceptanceAndNotify(AbstractGame abstractGame) throws UserNotFoundException, TeamNotFoundException {
        AbstractSinglesGame singlesGame = (AbstractSinglesGame) abstractGame;
        User user1 = userService.getUser(singlesGame.getPlayer1());
        User user2 = userService.getUser(singlesGame.getPlayer2());

        AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                AcceptanceRequestType.PASSIVE,
                singlesGame.getPlayer1(), abstractGame.getId(),abstractGame.getGameType());
        AcceptanceRequest activeAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                getAcceptanceRequestType(),
                singlesGame.getPlayer2(), abstractGame.getId(),abstractGame.getGameType());

        acceptanceRequestRepository.save(posterAcceptanceRequest);
        acceptanceRequestRepository.save(activeAcceptanceRequest);
        notificationService.notify(posterAcceptanceRequest, user2.getUsername());
        notificationService.notify(activeAcceptanceRequest, user1.getUsername());
    }

    @Transactional
    @Override
    public void acceptGame(UUID gameId) throws CapserException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        AcceptanceRequest request = extractAcceptanceRequest(gameId);
        AbstractGame game = findGame(request.getGameToAccept());
        game.setAccepted(true);
        updateEloAndStats(game, acceptanceRequestList);
        saveGame(game);
        notificationService.notify(Notification.builder()
                .date(new Date())
                .notificationType(NotificationType.GAME_ACCEPTED)
                .text("Game with user " + userService.getUser(SecurityUtils.getUserId()).getUsername()  + " was accepted by the user.")
                .seen(false)
                .userId(request.getAcceptingUser())
                .build());
    }

    @Transactional
    @Override
    public void rejectGame(UUID gameId) throws CapserException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        AcceptanceRequest request = extractAcceptanceRequest(gameId);
        AbstractGame game = findGame(request.getGameToAccept());
        removeGame(game);
        notificationService.notify(Notification.builder()
                .date(new Date())
                .notificationType(NotificationType.GAME_REJECTED)
                .text("Game with user " + userService.getUser(SecurityUtils.getUserId()).getUsername()  + " was rejected by the user.")
                .seen(false)
                .userId(request.getAcceptingUser())
                .build());
        acceptanceRequestRepository.deleteAll(acceptanceRequestList);

    }

    protected void updateEloAndStats(AbstractGame abstractGame, List<AcceptanceRequest> acceptanceRequestList) throws CapserException {
        AbstractSinglesGame abstractSinglesGame = (AbstractSinglesGame) abstractGame;
        User user1 = userService.getUser(abstractSinglesGame.getPlayer1());
        User user2 = userService.getUser(abstractSinglesGame.getPlayer2());

        abstractSinglesGame.calculatePlayerStats(user1);
        abstractSinglesGame.calculatePlayerStats(user2);

        boolean d;
        d = abstractSinglesGame.getWinner().equals(user1.getId());

        EloRating.EloResult eloResult = EloRating.calculate(abstractSinglesGame.findCorrectStats(user1).getPoints(), abstractSinglesGame.findCorrectStats(user2).getPoints(), 30,d);

        abstractSinglesGame.updateUserPoints(user1, eloResult.getResult1());
        abstractSinglesGame.updateUserPoints(user2, eloResult.getResult2());


        userService.saveUser(user1);
        userService.saveUser(user2);

        acceptanceRequestRepository.deleteAll(acceptanceRequestList);
    }


    private AcceptanceRequest extractAcceptanceRequest(UUID gameId) throws GameNotFoundException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        if (acceptanceRequestList.isEmpty()) {
            throw new GameNotFoundException("Cannot find game to accept with this id");
        }
        return  acceptanceRequestList.stream().filter(acceptanceRequest ->
                acceptanceRequest.getAcceptanceRequestType().equals(AcceptanceRequestType.PASSIVE)).findFirst().get();
    }


    public abstract AbstractGame saveGame(AbstractGame abstractGame);
    public abstract void removeGame(AbstractGame abstractGame);


}
