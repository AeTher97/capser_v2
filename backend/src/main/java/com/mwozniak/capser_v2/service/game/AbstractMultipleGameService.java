package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.NotificationType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.Notification;
import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.multiple.AbstractMultipleGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import com.mwozniak.capser_v2.service.EmailService;
import com.mwozniak.capser_v2.service.NotificationService;
import com.mwozniak.capser_v2.service.TeamService;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.utils.EloRating;
import com.mwozniak.capser_v2.utils.EmailLoader;
import lombok.extern.log4j.Log4j;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Log4j
public abstract class AbstractMultipleGameService extends AbstractGameService {

    private final TeamService teamService;
    private final EmailService emailService;

    public AbstractMultipleGameService(AcceptanceRequestRepository acceptanceRequestRepository, EmailService emailService, UserService userService, NotificationService notificationService, TeamService teamService) {
        super(acceptanceRequestRepository, userService, emailService, notificationService);
        this.teamService = teamService;
        this.emailService = emailService;
    }

    @Transactional
    @Override
    public UUID queueGame(AbstractGame abstractGame) throws CapserException {
        AbstractMultipleGame abstractMultipleGame = (AbstractMultipleGame) abstractGame;
        teamService.findTeam(abstractMultipleGame.getTeam1DatabaseId());
        teamService.findTeam(abstractMultipleGame.getTeam2DatabaseId());
        AbstractGame saved = saveGame(abstractGame);
        addAcceptanceAndNotify(saved);
        return saved.getId();
    }

    @Override
    protected void addAcceptanceAndNotify(AbstractGame abstractGame) throws UserNotFoundException, TeamNotFoundException {
        AbstractMultipleGame multipleGame = (AbstractMultipleGame) abstractGame;

        String winnerName = teamService.getTeam(multipleGame.getWinnerTeamId()).getName();
        String loserName = teamService.getTeam(multipleGame.getLoserTeamId()).getName();

        if (multipleGame.getWinner().getPlayerList().contains(SecurityUtils.getUserId())) {
            for (UUID id : multipleGame.getWinner().getPlayerList()) {
                AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                        AcceptanceRequestType.PASSIVE,
                        id, abstractGame.getId(), abstractGame.getGameType());
                acceptanceRequestRepository.save(posterAcceptanceRequest);
                notificationService.notifyMultiple(posterAcceptanceRequest, loserName);

            }
            for (UUID id : multipleGame.getLoser().getPlayerList()) {
                User user = userService.getUser(id);
                AcceptanceRequest receiverAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                        getAcceptanceRequestType(),
                        id, abstractGame.getId(), abstractGame.getGameType(), multipleGame.getLoserTeamId());
                acceptanceRequestRepository.save(receiverAcceptanceRequest);
                notificationService.notifyMultiple(receiverAcceptanceRequest, winnerName);
                emailService.sendHtmlMessage(user.getEmail(), "New Doubles Game in GCL!", EmailLoader.loadGameAcceptanceEmail().replace("${player}", user.getUsername()).replace("${opponent}", "team " + winnerName));

            }
        } else {
            for (UUID id : multipleGame.getWinner().getPlayerList()) {
                User user = userService.getUser(id);
                AcceptanceRequest receiverAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                        getAcceptanceRequestType(),
                        id, abstractGame.getId(), abstractGame.getGameType(), multipleGame.getWinnerTeamId());
                acceptanceRequestRepository.save(receiverAcceptanceRequest);
                notificationService.notifyMultiple(receiverAcceptanceRequest, loserName);
                emailService.sendHtmlMessage(user.getEmail(), "New Doubles Game in GCL!", EmailLoader.loadGameAcceptanceEmail().replace("${player}", user.getUsername()).replace("${opponent}", "team " + loserName));


            }
            for (UUID id : multipleGame.getLoser().getPlayerList()) {
                AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                        AcceptanceRequestType.PASSIVE,
                        id, abstractGame.getId(), abstractGame.getGameType());
                acceptanceRequestRepository.save(posterAcceptanceRequest);
                notificationService.notifyMultiple(posterAcceptanceRequest, winnerName);
            }
        }

    }

    @Override
    protected void updateEloAndStats(AbstractGame abstractGame) throws CapserException {
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

        multipleGame.calculateTeamStats(winner.getDoublesStats(),
                multipleGame.getWinnerTeamId().equals(multipleGame.getTeam1DatabaseId()) ? multipleGame.getTeam1Stats() : multipleGame.getTeam2Stats(), true ,eloResult.getResult1());
        multipleGame.calculateTeamStats(loser.getDoublesStats(),
                multipleGame.getLoserTeamId().equals(multipleGame.getTeam1DatabaseId()) ? multipleGame.getTeam1Stats() : multipleGame.getTeam2Stats(), false ,eloResult.getResult2());

    }

    @Transactional
    @Override
    public AbstractGame acceptGame(UUID gameId) throws CapserException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        AcceptanceRequest request = extractAcceptanceRequest(gameId);
        AbstractGame game = findGame(request.getGameToAccept());
        game.setAccepted(true);
        updateEloAndStats(game);
        acceptanceRequestRepository.deleteAll(acceptanceRequestList);
        saveGame(game);
        acceptanceRequestList.stream().filter(acceptanceRequest -> acceptanceRequest.getAcceptanceRequestType().equals(AcceptanceRequestType.PASSIVE)).forEach(acceptanceRequest -> {
            try {
                notificationService.notify(Notification.builder()
                        .date(new Date())
                        .notificationType(NotificationType.GAME_ACCEPTED)
                        .text("Game with team " + teamService.findTeam(request.getAcceptingTeam()).getName()  + " was accepted.")
                        .seen(false)
                        .userId(acceptanceRequest.getAcceptingUser())
                        .build());
            } catch (CapserException e) {
                log.error("Failed to get team name.");
            }
        });
        return game;
    }

    @Transactional
    @Override
    public void rejectGame(UUID gameId) throws CapserException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        AcceptanceRequest request = extractAcceptanceRequest(gameId);
        AbstractGame game = findGame(request.getGameToAccept());
        removeGame(game);
        acceptanceRequestList.stream().filter(acceptanceRequest -> acceptanceRequest.getAcceptanceRequestType().equals(AcceptanceRequestType.PASSIVE)).forEach(acceptanceRequest -> {
            try {
                notificationService.notify(Notification.builder()
                        .date(new Date())
                        .notificationType(NotificationType.GAME_REJECTED)
                        .text("Game with team " +  teamService.findTeam(request.getAcceptingTeam()).getName() + " was rejected by on of the users.")
                        .seen(false)
                        .userId(acceptanceRequest.getAcceptingUser())
                        .build());
            } catch (CapserException e) {
                log.error("Failed to get team name.");
            }
        });
        acceptanceRequestRepository.deleteAll(acceptanceRequestList);

    }

    @Override
    public AcceptanceRequestType getAcceptanceRequestType() {
        return AcceptanceRequestType.MULTIPlE;
    }
}
