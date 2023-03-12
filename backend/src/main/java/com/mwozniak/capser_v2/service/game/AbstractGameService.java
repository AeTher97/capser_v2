package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.NotificationType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.Notification;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.Game;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSoloGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.service.AchievementService;
import com.mwozniak.capser_v2.service.EmailService;
import com.mwozniak.capser_v2.service.NotificationService;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.utils.EmailLoader;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
public abstract class AbstractGameService<T extends Game> implements GameService<T> {

    protected final AcceptanceRequestRepository acceptanceRequestRepository;
    protected final UserService userService;
    protected final AchievementService achievementService;
    private final EmailService emailService;

    protected final NotificationService notificationService;


    protected AbstractGameService(AcceptanceRequestRepository acceptanceRequestRepository,
                                  UserService userService,
                                  AchievementService achievementService, EmailService emailService, NotificationService notificationService) {
        this.acceptanceRequestRepository = acceptanceRequestRepository;
        this.userService = userService;
        this.achievementService = achievementService;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    @Transactional
    @Override
    public void queueGame(T game) throws CapserException {
        queueGame(game, true);
    }

    @Transactional
    @Override
    public void queueGame(T game, boolean notify) throws CapserException {
        T saved = saveGame(game);
        if (notify) {
            addAcceptanceAndNotify(saved);
        }
    }

    protected void addAcceptanceAndNotify(T game) throws UserNotFoundException, TeamNotFoundException {
        AbstractSoloGame singlesGame = (AbstractSoloGame) game;
        User user1 = userService.getUser(singlesGame.getPlayer1());
        User user2 = userService.getUser(singlesGame.getPlayer2());

        AcceptanceRequest posterAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                AcceptanceRequestType.PASSIVE,
                singlesGame.getPlayer1(), game.getId(), game.getGameType());
        AcceptanceRequest activeAcceptanceRequest = AcceptanceRequest.createAcceptanceRequest(
                getAcceptanceRequestType(),
                singlesGame.getPlayer2(), game.getId(), game.getGameType());

        acceptanceRequestRepository.save(posterAcceptanceRequest);
        acceptanceRequestRepository.save(activeAcceptanceRequest);
        notificationService.notify(posterAcceptanceRequest, user2.getUsername());
        notificationService.notify(activeAcceptanceRequest, user1.getUsername());
        emailService.sendHtmlMessage(user2.getEmail(), "New game in GCL!",
                EmailLoader.loadGameAcceptanceEmail().replace("${player}",
                        user2.getUsername()).replace("${opponent}", user1.getUsername()));
    }

    private void processAchievements(T game) {
        List<UUID> players = game.getAllPlayers();
        players.forEach(player -> {
            User user = userService.getUser(player);
            doProcessAchievements(user, game);
        });
    }

    protected abstract void doProcessAchievements(User user, T game);

    @Transactional
    @Override
    public void acceptGame(UUID gameId) throws CapserException {
        acceptGame(gameId, true);
    }

    @Transactional
    @Override
    public void acceptGame(UUID gameId, boolean notify) throws CapserException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        AcceptanceRequest request = extractAcceptanceRequest(gameId);
        T game = findGame(request.getGameToAccept());
        game.setAccepted();
        updateEloAndStats(game);
        processAchievements(game);
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
    }

    @Transactional
    public T postGameWithoutAcceptance(T game) throws CapserException {
        game.setAccepted();
        updateEloAndStats(game);
        processAchievements(game);
        return saveGame(game);
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
        T game = findGame(request.getGameToAccept());
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


    @Override
    public Page<T> listAcceptedGames(Pageable pageable, UUID player) {
        if (player == null) {
            return listAcceptedGames(pageable);
        } else {
            return listPlayerAcceptedGames(pageable, player);
        }
    }

    public abstract Page<T> listPlayerAcceptedGames(Pageable pageable, UUID player);

    protected AcceptanceRequest extractAcceptanceRequest(UUID gameId) throws GameNotFoundException {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        if (acceptanceRequestList.isEmpty()) {
            throw new GameNotFoundException("Cannot find game to accept with this id");
        }
        return acceptanceRequestList.stream().filter(acceptanceRequest ->
                acceptanceRequest.getAcceptanceRequestType().equals(getAcceptanceRequestType())).findFirst().get();
    }


}
