package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.multiple.AbstractMultipleGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.utils.EloRating;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

public abstract class AbstractMultipleGameService extends AbstractGameService {

    private final TeamService teamService;

    public AbstractMultipleGameService(AcceptanceRequestRepository acceptanceRequestRepository, UserService userService, NotificationService notificationService, TeamService teamService) {
        super(acceptanceRequestRepository, userService, notificationService);
        this.teamService = teamService;
    }

    @Transactional
    @Override
    public void queueGame(AbstractGame abstractGame) throws CapserException {
        AbstractMultipleGame abstractMultipleGame = (AbstractMultipleGame) abstractGame;
        teamService.findTeam(abstractMultipleGame.getTeam1DatabaseId());
        teamService.findTeam(abstractMultipleGame.getTeam2DatabaseId());
        AbstractGame saved = saveGame(abstractGame);
        addAcceptanceAndNotify(saved);
    }

    @Override
    protected void addAcceptanceAndNotify(AbstractGame abstractGame) throws UserNotFoundException {
        AbstractMultipleGame multipleGame = (AbstractMultipleGame) abstractGame;

        for (UUID id : multipleGame.getWinner().getPlayerList()) {
            User user = userService.getUser(id);
            AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                    AcceptanceRequestType.PASSIVE,
                    id, abstractGame.getId(),abstractGame.getGameType());
            acceptanceRequestRepository.save(posterAcceptanceRequest);
            notificationService.notify(posterAcceptanceRequest, user.getUsername());

        }

        for (UUID id : multipleGame.getWinner().getPlayerList()) {
            User user = userService.getUser(id);
            AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                    getAcceptanceRequestType(),
                    id, abstractGame.getId(),abstractGame.getGameType());
            acceptanceRequestRepository.save(posterAcceptanceRequest);
            notificationService.notify(posterAcceptanceRequest, user.getUsername());
        }

    }

    @Override
    protected void updateEloAndStats(AbstractGame abstractGame, List<AcceptanceRequest> acceptanceRequestList) throws CapserException {
        AbstractMultipleGame multipleGame = (AbstractMultipleGame) abstractGame;

        TeamWithStats winner = teamService.findTeam(multipleGame.getWinnerTeamId());
        TeamWithStats loser = teamService.findTeam(multipleGame.getLoserTeamId());

        EloRating.EloResult eloResult = EloRating.calculate(winner.getDoublesStats().getPoints(), loser.getDoublesStats().getPoints(), 30, true);

        for (UUID id : multipleGame.getWinner().getPlayerList()) {
            User user = userService.getUser(id);
            multipleGame.calculatePlayerStats(user);
            multipleGame.updateUserPoints(user, eloResult.getResult1());

            userService.saveUser(user);

        }

        for (UUID id : multipleGame.getLoser().getPlayerList()) {
            User user = userService.getUser(id);
            multipleGame.calculatePlayerStats(user);
            multipleGame.updateUserPoints(user, eloResult.getResult2());

            userService.saveUser(user);

        }

        acceptanceRequestRepository.deleteAll(acceptanceRequestList);
    }


    @Override
    public AcceptanceRequestType getAcceptanceRequestType() {
        return AcceptanceRequestType.MULTIPlE;
    }
}
