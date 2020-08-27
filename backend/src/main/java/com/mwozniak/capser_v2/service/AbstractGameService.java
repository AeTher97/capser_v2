package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.AbstractSinglesGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.utils.EloRating;

import javax.transaction.Transactional;
import java.awt.print.Pageable;
import java.util.*;

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
        AbstractGame saved = saveGame(abstractGame);
        addAcceptanceAndNotify(saved);
    }

    protected void addAcceptanceAndNotify(AbstractGame abstractGame) throws UserNotFoundException {
        AbstractSinglesGame singlesGame = (AbstractSinglesGame) abstractGame;
        User user1 = userService.getUser(singlesGame.getPlayer1());
        User user2 = userService.getUser(singlesGame.getPlayer2());

        AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                AcceptanceRequestType.PASSIVE,
                singlesGame.getPlayer1(), abstractGame.getId());
        AcceptanceRequest activeAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                getAcceptanceRequestType(),
                singlesGame.getPlayer2(), abstractGame.getId());

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
        AbstractGame game = findGame(request.getGameToAccept());
        game.setAccepted(true);
        updateEloAndStats(game, acceptanceRequestList);
        saveGame(game);
    }

    protected void updateEloAndStats(AbstractGame abstractGame, List<AcceptanceRequest> acceptanceRequestList) throws CapserException {
        AbstractSinglesGame singlesGame = (AbstractSinglesGame) abstractGame;
        User user1 = userService.getUser(singlesGame.getPlayer1());
        User user2 = userService.getUser(singlesGame.getPlayer2());

        boolean d;
        d = singlesGame.getWinner().equals(user1.getId());

        List<User> listToPass = Arrays.asList(user1, user2);
        float player1PreviousRating = user1.getUserSinglesStats().getPoints();
        float player2PreviousRating = user2.getUserSinglesStats().getPoints();

        ArrayList<User> listToPassClone = new ArrayList<>();

        Iterator<User> iterator = listToPass.iterator();
        while(iterator.hasNext()){
            listToPassClone.add(iterator.next().clone());
        }

        EloRating.calculate(listToPassClone, 30, d);

        float player1PointsChange = user1.getUserSinglesStats().getPoints() - player1PreviousRating;
        float player2PointsChange = user2.getUserSinglesStats().getPoints() - player2PreviousRating;

        singlesGame.getPlayer1Stats().setPointsChange(player1PointsChange);
        singlesGame.getPlayer2Stats().setPointsChange(player2PointsChange);

        singlesGame.calculatePlayerStats(user1);
        singlesGame.calculatePlayerStats(user2);

        singlesGame.updateUserPoints(user1,player1PointsChange);
        singlesGame.updateUserPoints(user2,player2PointsChange);

        userService.saveUser(user1);
        userService.saveUser(user2);

        acceptanceRequestRepository.deleteAll(acceptanceRequestList);
    }


    public abstract AbstractGame saveGame(AbstractGame abstractGame);


}
