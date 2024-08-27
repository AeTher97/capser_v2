package com.mwozniak.capser_v2.utils;

import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSoloGame;
import com.mwozniak.capser_v2.models.dto.PlayerComparisonDto;

import java.util.HashSet;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PlayerComparisonCollector implements Collector<AbstractSoloGame, PlayerComparisonDto, PlayerComparisonDto> {
    @Override
    public Supplier<PlayerComparisonDto> supplier() {
        return PlayerComparisonDto::new;
    }

    @Override
    public BiConsumer<PlayerComparisonDto, AbstractSoloGame> accumulator() {
        return ((playerComparisonDto, abstractSoloGame) -> {
            fillIdFieldsIfRequired(playerComparisonDto, abstractSoloGame);

            playerComparisonDto.setGamesPlayed(playerComparisonDto.getGamesPlayed() + 1);

            accumulatePlayerStats(playerComparisonDto.getPlayer1Stats(),
                    abstractSoloGame.getPlayer1() == playerComparisonDto.getPlayer1Id() ? abstractSoloGame.getPlayer1Stats()
                            : abstractSoloGame.getPlayer2Stats(),
                    abstractSoloGame.getWinner().equals(playerComparisonDto.getPlayer1Id()));
            accumulatePlayerStats(playerComparisonDto.getPlayer2Stats(),
                    abstractSoloGame.getPlayer2() == playerComparisonDto.getPlayer2Id() ? abstractSoloGame.getPlayer2Stats()
                            : abstractSoloGame.getPlayer1Stats(),
                    abstractSoloGame.getWinner().equals(playerComparisonDto.getPlayer2Id()));
        });
    }

    @Override
    public BinaryOperator<PlayerComparisonDto> combiner() {
        return (playerComparisonDto, playerComparisonDto2) -> {
            playerComparisonDto.setGamesPlayed(playerComparisonDto.getGamesPlayed()
                    + playerComparisonDto2.getGamesPlayed());
            combinePlayerStats(playerComparisonDto.getPlayer1Stats(),
                    playerComparisonDto.getPlayer1Id().equals(playerComparisonDto2.getPlayer1Id())
                            ? playerComparisonDto2.getPlayer1Stats() : playerComparisonDto.getPlayer2Stats());

            combinePlayerStats(playerComparisonDto.getPlayer2Stats(),
                    playerComparisonDto.getPlayer2Id().equals(playerComparisonDto2.getPlayer2Id())
                    ? playerComparisonDto2.getPlayer2Stats() : playerComparisonDto.getPlayer1Stats());
            return playerComparisonDto;
        };
    }

    @Override
    public Function<PlayerComparisonDto, PlayerComparisonDto> finisher() {
        return a -> a;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
    }

    private void fillIdFieldsIfRequired(PlayerComparisonDto playerComparisonDto, AbstractSoloGame abstractSoloGame) {
        if (playerComparisonDto.getPlayer1() == null) {
            playerComparisonDto.setPlayer1Id(abstractSoloGame.getPlayer1());
        }
        if (playerComparisonDto.getPlayer2() == null) {
            playerComparisonDto.setPlayer2Id(abstractSoloGame.getPlayer2());
        }
    }

    private void accumulatePlayerStats(PlayerComparisonDto.ComparisonStats comparisonStats, GamePlayerStats stats, boolean won) {
        if (won) {
            comparisonStats.setGamesWon(comparisonStats.getGamesWon() + 1);
        } else {
            comparisonStats.setGamesLost(comparisonStats.getGamesLost() + 1);
        }
        comparisonStats.setPointsMade(comparisonStats.getPointsMade() + stats.getScore());
        comparisonStats.setRebuttalsMade(comparisonStats.getRebuttalsMade() + stats.getRebuttals());
        comparisonStats.setSinksMade(comparisonStats.getSinksMade() + stats.getSinks());
        comparisonStats.setPointsChange(comparisonStats.getPointsChange() + stats.getPointsChange());
        comparisonStats.setNakedLaps(comparisonStats.getNakedLaps() + (stats.isNakedLap() ? 1 : 0));
        comparisonStats.setBeersDowned(comparisonStats.getBeersDowned() + stats.getBeersDowned());
    }

    private void combinePlayerStats(PlayerComparisonDto.ComparisonStats oldStats, PlayerComparisonDto.ComparisonStats newStats) {
        oldStats.setGamesWon(oldStats.getGamesWon() + newStats.getGamesWon());
        oldStats.setGamesLost(oldStats.getGamesLost() + newStats.getGamesLost());
        oldStats.setPointsMade(oldStats.getPointsMade() + newStats.getPointsMade());
        oldStats.setRebuttalsMade(oldStats.getRebuttalsMade() + newStats.getRebuttalsMade());
        oldStats.setSinksMade(oldStats.getSinksMade() + newStats.getSinksMade());
        oldStats.setPointsChange(oldStats.getPointsChange() + newStats.getPointsChange());
        oldStats.setNakedLaps(oldStats.getNakedLaps() + newStats.getNakedLaps());
        oldStats.setBeersDowned(oldStats.getBeersDowned() + newStats.getBeersDowned());
    }
}
