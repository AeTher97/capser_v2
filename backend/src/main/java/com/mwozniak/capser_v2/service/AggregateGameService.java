package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.service.game.EasyCapsGameService;
import com.mwozniak.capser_v2.service.game.SinglesGameService;
import com.mwozniak.capser_v2.service.game.UnrankedGameService;
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

    public AggregateGameService(SinglesGameService singlesGameService, EasyCapsGameService easyCapsGameService, UnrankedGameService unrankedGameService) {
        this.singlesGameService = singlesGameService;
        this.easyCapsGameService = easyCapsGameService;
        this.unrankedGameService = unrankedGameService;
    }


    public Page<AbstractGame> getUserGames(UUID userId, int pageNumber) {
        pageNumber += 1;
        List<AbstractGame> singlesGames = singlesGameService.listPlayerAcceptedGames(PageRequest.of(0, Integer.MAX_VALUE, Sort.by("time").descending()), userId).getContent();
        List<AbstractGame> easyCapsGames = easyCapsGameService.listPlayerAcceptedGames(PageRequest.of(0, Integer.MAX_VALUE, Sort.by("time").descending()), userId).getContent();
        List<AbstractGame> unrankedGames = unrankedGameService.listPlayerAcceptedGames(PageRequest.of(0, Integer.MAX_VALUE, Sort.by("time").descending()), userId).getContent();


        List<AbstractGame> aggregatedList = new ArrayList<>();
        aggregatedList.addAll(singlesGames);
        aggregatedList.addAll(easyCapsGames);
        aggregatedList.addAll(unrankedGames);


        aggregatedList.sort(AbstractGame.Comparators.DATE);
        Collections.reverse(aggregatedList);

        if (aggregatedList.size() > 10 * pageNumber) {
            return new PageImpl<>(aggregatedList.subList(10 * pageNumber - 10, 10 * pageNumber - 1), PageRequest.of(0, 10), aggregatedList.size());
        } else {
            return new PageImpl<>(aggregatedList.subList(10 * pageNumber - 10, aggregatedList.size() - 1), PageRequest.of(0, 10), aggregatedList.size());
        }
    }

    public Page<? extends AbstractGame> getUserGames(UUID userId, UUID user2Id, GameType gameType, int page) {

        switch (gameType) {
            case SINGLES:
                return singlesGameService.listGamesWithPlayerAndOpponent(PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId, user2Id);
            case EASY_CAPS:
                return easyCapsGameService.listGamesWithPlayerAndOpponent(PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId, user2Id);
            case UNRANKED:
                return unrankedGameService.listGamesWithPlayerAndOpponent(PageRequest.of(page, 10, Sort.by("time").descending()),
                        userId, user2Id);
            default:
                return null;
        }
    }
}
