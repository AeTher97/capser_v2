package com.mwozniak.capser_v2.service.game;

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
import com.mwozniak.capser_v2.service.EmailService;
import com.mwozniak.capser_v2.service.NotificationService;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.utils.EloRating;
import com.mwozniak.capser_v2.utils.EmailLoader;
import lombok.extern.log4j.Log4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j
public abstract class AbstractGameService implements GameService {

    protected final AcceptanceRequestRepository acceptanceRequestRepository;
    protected final UserService userService;
    private final EmailService emailService;

    protected final NotificationService notificationService;


    public AbstractGameService(AcceptanceRequestRepository acceptanceRequestRepository,
                               UserService userService,
                               EmailService emailService, NotificationService notificationService) {
        this.acceptanceRequestRepository = acceptanceRequestRepository;
        this.userService = userService;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @Override
    public UUID queueGame(AbstractGame abstractGame) throws CapserException {
        return queueGame(abstractGame, true);
    }

    @Transactional
    @Override
    public UUID queueGame(AbstractGame abstractGame, boolean notify) throws CapserException {
        AbstractGame saved = saveGame(abstractGame);
        if (notify) {
            addAcceptanceAndNotify(saved);
        }
        return saved.getId();
    }

    protected void addAcceptanceAndNotify(AbstractGame abstractGame) throws UserNotFoundException, TeamNotFoundException {
        AbstractSinglesGame singlesGame = (AbstractSinglesGame) abstractGame;
        User user1 = userService.getUser(singlesGame.getPlayer1());
        User user2 = userService.getUser(singlesGame.getPlayer2());

        AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                AcceptanceRequestType.PASSIVE,
                singlesGame.getPlayer1(), abstractGame.getId(), abstractGame.getGameType());
        AcceptanceRequest activeAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                getAcceptanceRequestType(),
                singlesGame.getPlayer2(), abstractGame.getId(), abstractGame.getGameType());

        acceptanceRequestRepository.save(posterAcceptanceRequest);
        acceptanceRequestRepository.save(activeAcceptanceRequest);
        notificationService.notify(posterAcceptanceRequest, user2.getUsername());
        notificationService.notify(activeAcceptanceRequest, user1.getUsername());
        emailService.sendHtmlMessage(user2.getEmail(), "New game in GCL!",
                EmailLoader.loadGameAcceptanceEmail().replace("${player}",
                        user2.getUsername()).replace("${opponent}", user1.getUsername()));
    }

    @Override
    public AbstractGame acceptGame(UUID gameId) throws CapserException {
        return acceptGame(gameId, true);
    }

    @Transactional
    @Override
    public AbstractGame acceptGame(UUID gameId, boolean notify) throws CapserException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        AcceptanceRequest request = extractAcceptanceRequest(gameId);
        AbstractGame game = findGame(request.getGameToAccept());
        game.setAccepted(true);
        updateEloAndStats(game);
        acceptanceRequestRepository.deleteAll(acceptanceRequestList);
        saveGame(game);
        if (notify) {
            notificationService.notify(Notification.builder()
                    .date(new Date())
                    .notificationType(NotificationType.GAME_ACCEPTED)
                    .text("Game with " + userService.getUser(request.getAcceptingUser()).getUsername() + " was accepted.")
                    .seen(false)
                    .userId(acceptanceRequestList.stream()
                            .filter(acceptanceRequest -> acceptanceRequest.getAcceptanceRequestType()
                                    .equals(AcceptanceRequestType.PASSIVE))
                            .findFirst().get().getAcceptingUser())
                    .build());
        }
        return game;
    }

    @Transactional
    public AbstractGame postGameWithoutAcceptance(AbstractGame abstractGame) throws CapserException {
        abstractGame.setAccepted(true);
        updateEloAndStats(abstractGame);
        return saveGame(abstractGame);
    }

    @Override
    @Transactional
    public int acceptOverdueGames() {
        Date date = Date.from(Instant.now().minus(Duration.ofDays(1)));
        List<AcceptanceRequest> acceptanceRequests = acceptanceRequestRepository.findByTimestampLessThanAndGameType(date, getGameType());

        AtomicInteger accepted = new AtomicInteger(acceptanceRequests.size());

        acceptanceRequests.forEach(request -> {
            try {
                acceptGame(request.getGameToAccept());
            } catch (CapserException e) {
                log.error("Failed to accept game " + request.getGameToAccept() + " " + e.getMessage());
                accepted.addAndGet(-1);
            }
        });

        log.info("Accepted " + accepted + " " + getGameType() + " games");

        return accepted.get();
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
                .text("Game with " + userService.getUser(request.getAcceptingUser()).getUsername() + " was rejected by the user.")
                .seen(false)
                .userId(acceptanceRequestList.stream()
                        .filter(acceptanceRequest -> acceptanceRequest.getAcceptanceRequestType()
                                .equals(AcceptanceRequestType.PASSIVE))
                        .findFirst().get().getAcceptingUser())
                .build());
        acceptanceRequestRepository.deleteAll(acceptanceRequestList);

    }

    protected void updateEloAndStats(AbstractGame abstractGame) throws CapserException {
        AbstractSinglesGame abstractSinglesGame = (AbstractSinglesGame) abstractGame;
        User user1 = userService.getUser(abstractSinglesGame.getPlayer1());
        User user2 = userService.getUser(abstractSinglesGame.getPlayer2());

        abstractSinglesGame.calculatePlayerStats(user1);
        abstractSinglesGame.calculatePlayerStats(user2);

        boolean d;
        d = abstractSinglesGame.getWinner().equals(user1.getId());

        EloRating.EloResult eloResult = EloRating.calculate(abstractSinglesGame.findCorrectStats(user1).getPoints(), abstractSinglesGame.findCorrectStats(user2).getPoints(), 30, d);

        abstractSinglesGame.updateUserPoints(user1, eloResult.getResult1());
        abstractSinglesGame.updateUserPoints(user2, eloResult.getResult2());


        userService.saveUser(user1);
        userService.saveUser(user2);

    }


    @Override
    public Page<AbstractGame> listAcceptedGames(Pageable pageable, UUID player) {
        if (player == null) {
            return listAcceptedGames(pageable);
        } else {
            return listPlayerAcceptedGames(pageable, player);
        }
    }

    protected abstract Page<AbstractGame> listPlayerAcceptedGames(Pageable pageable, UUID player);

    protected AcceptanceRequest extractAcceptanceRequest(UUID gameId) throws GameNotFoundException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        if (acceptanceRequestList.isEmpty()) {
            throw new GameNotFoundException("Cannot find game to accept with this id");
        }
        return acceptanceRequestList.stream().filter(acceptanceRequest ->
                acceptanceRequest.getAcceptanceRequestType().equals(getAcceptanceRequestType())).findFirst().get();
    }


    public abstract AbstractGame saveGame(AbstractGame abstractGame);

    public abstract void removeGame(AbstractGame abstractGame);


}
