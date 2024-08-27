package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSoloGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.models.dto.PlayerComparisonDto;
import com.mwozniak.capser_v2.models.responses.UserMinimized;
import com.mwozniak.capser_v2.service.game.EasyCapsGameService;
import com.mwozniak.capser_v2.service.game.SinglesGameService;
import com.mwozniak.capser_v2.service.game.UnrankedGameService;
import com.mwozniak.capser_v2.utils.PlayerComparisonCollector;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AggregateGameService {


    private final SinglesGameService singlesGameService;
    private final EasyCapsGameService easyCapsGameService;
    private final UnrankedGameService unrankedGameService;
    private final UserService userService;

    public AggregateGameService(SinglesGameService singlesGameService,
                                EasyCapsGameService easyCapsGameService,
                                UnrankedGameService unrankedGameService,
                                UserService userService) {
        this.singlesGameService = singlesGameService;
        this.easyCapsGameService = easyCapsGameService;
        this.unrankedGameService = unrankedGameService;
        this.userService = userService;
    }


    public Page<AbstractGame> getUserGamesWithTypeAndOpponent(UUID userId, int pageNumber) {
        pageNumber += 1;
        List<SoloGame> singlesGames = singlesGameService.listPlayerAcceptedGames(PageRequest.of(0, Integer.MAX_VALUE, Sort.by("time").descending()), userId).getContent();
        List<EasyCapsGame> easyCapsGames = easyCapsGameService.listPlayerAcceptedGames(PageRequest.of(0, Integer.MAX_VALUE, Sort.by("time").descending()), userId).getContent();
        List<UnrankedGame> unrankedGames = unrankedGameService.listPlayerAcceptedGames(PageRequest.of(0, Integer.MAX_VALUE, Sort.by("time").descending()), userId).getContent();


        List<AbstractGame> aggregatedList = new ArrayList<>();
        aggregatedList.addAll(singlesGames);
        aggregatedList.addAll(easyCapsGames);
        aggregatedList.addAll(unrankedGames);


        aggregatedList.sort(AbstractGame.Comparators.DATE);
        Collections.reverse(aggregatedList);

        if (aggregatedList.size() > 10 * pageNumber) {
            return new PageImpl<>(aggregatedList.subList(10 * pageNumber - 10, 10 * pageNumber), PageRequest.of(0, 10), aggregatedList.size());
        } else {
            return new PageImpl<>(aggregatedList.subList(10 * pageNumber - 10, aggregatedList.size()), PageRequest.of(0, 10), aggregatedList.size());
        }
    }

    public Page<? extends AbstractGame> getUserGamesWithTypeAndOpponent(UUID userId, UUID user2Id, GameType gameType, int page) {

        switch (gameType) {
            case SINGLES:
                return singlesGameService.listGamesWithPlayerAndOpponent(
                        PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId, user2Id);
            case EASY_CAPS:
                return easyCapsGameService.listGamesWithPlayerAndOpponent(
                        PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId, user2Id);
            case UNRANKED:
                return unrankedGameService.listGamesWithPlayerAndOpponent(
                        PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId, user2Id);
            default:
                return null;
        }
    }

    public PlayerComparisonDto getPlayerComparison(UUID userId, UUID user2Id, GameType gameType) {
        List<? extends AbstractSoloGame> games = new ArrayList<>();
        switch (gameType) {
            case SINGLES:
                games = singlesGameService.listGamesWithPlayerAndOpponent(userId, user2Id);
                break;
            case EASY_CAPS:
                games = easyCapsGameService.listGamesWithPlayerAndOpponent(userId, user2Id);
                break;
            case UNRANKED:
                games = unrankedGameService.listGamesWithPlayerAndOpponent(userId, user2Id);
        }

        PlayerComparisonDto playerComparisonDto =  games.stream().collect(new PlayerComparisonCollector());
        playerComparisonDto.setPlayer1(UserMinimized.fromUser(userService.getUser(playerComparisonDto.getPlayer1Id())));
        playerComparisonDto.setPlayer2(UserMinimized.fromUser(userService.getUser(playerComparisonDto.getPlayer2Id())));
        return playerComparisonDto;
    }

    public Page<? extends AbstractGame> getUserGamesWithType(UUID userId, GameType gameType, int page) {

        switch (gameType) {
            case SINGLES:
                return singlesGameService.listPlayerAcceptedGames(
                        PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId);
            case EASY_CAPS:
                return easyCapsGameService.listPlayerAcceptedGames(
                        PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId);
            case UNRANKED:
                return unrankedGameService.listPlayerAcceptedGames(
                        PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId);
            default:
                return null;
        }
    }
}
