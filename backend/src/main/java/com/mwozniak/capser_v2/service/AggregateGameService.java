package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.repository.EasyCapsRepository;
import com.mwozniak.capser_v2.repository.SinglesRepository;
import com.mwozniak.capser_v2.repository.UnrankedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class AggregateGameService {


    private final SinglesRepository singlesRepository;
    private final EasyCapsRepository easyCapsRepository;
    private final UnrankedRepository unrankedRepository;

    public AggregateGameService(SinglesRepository singlesRepository, EasyCapsRepository easyCapsRepository, UnrankedRepository unrankedRepository) {
        this.singlesRepository = singlesRepository;
        this.easyCapsRepository = easyCapsRepository;
        this.unrankedRepository = unrankedRepository;
    }

    public List<AbstractGame> getUserGames(UUID userId) {
        List<AbstractGame> singlesGames = (List<AbstractGame>) (List<?>) singlesRepository
                .findSinglesGamesByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(PageRequest.of(0, 10, Sort.by("time").descending()), userId, userId).getContent();
        List<AbstractGame> easyCapsGames = (List<AbstractGame>) (List<?>) easyCapsRepository
                .findEasyCapsGamesByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(PageRequest.of(0, 10, Sort.by("time").descending()), userId, userId).getContent();
        List<AbstractGame> unrankedGames = (List<AbstractGame>) (List<?>) unrankedRepository
                .findUnrankedGameByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(PageRequest.of(0, 10, Sort.by("time").descending()), userId, userId).getContent();

        List<AbstractGame> aggregatedList = new ArrayList<>();
        aggregatedList.addAll(singlesGames);
        aggregatedList.addAll(easyCapsGames);
        aggregatedList.addAll(unrankedGames);


        aggregatedList.sort(AbstractGame.Comparators.DATE);
        Collections.reverse(aggregatedList);
        if (aggregatedList.size() > 10) {
            return aggregatedList.subList(0, 9);
        } else {
            return aggregatedList;
        }
    }

    public Page<? extends AbstractGame> getUserGames(UUID userId, UUID user2Id, GameType gameType) {

        switch (gameType) {
            case SINGLES:
                return singlesRepository.findSinglesGamesWithPlayerAndOpponent(PageRequest.of(0, 10, Sort.by("time").descending()),
                        userId, user2Id);
            case EASY_CAPS:
                return easyCapsRepository.findEasyGamesWithPlayerAndOpponent(PageRequest.of(0, 10, Sort.by("time").descending()),
                        userId, user2Id);
            case UNRANKED:
                return unrankedRepository.findUnrankedGamesWithPlayerAndOpponent(PageRequest.of(0, 10, Sort.by("time").descending()),
                        userId, user2Id);
            default:
                return null;
        }
    }
}
