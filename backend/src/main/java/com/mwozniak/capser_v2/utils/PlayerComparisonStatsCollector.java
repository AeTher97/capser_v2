package com.mwozniak.capser_v2.utils;

import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.database.game.single.AbstractSoloGame;
import com.mwozniak.capser_v2.models.dto.PlayerComparisonDto;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public class PlayerComparisonStatsCollector implements Collector<AbstractSoloGame, PlayerComparisonDto.ComparisonStats,
        PlayerComparisonDto.ComparisonStats> {

    private final UUID playerId;

    public PlayerComparisonStatsCollector(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public Supplier<PlayerComparisonDto.ComparisonStats> supplier() {
        return PlayerComparisonDto.ComparisonStats::new;
    }

    @Override
    public BiConsumer<PlayerComparisonDto.ComparisonStats, AbstractSoloGame> accumulator() {
        return ((comparisonStats, abstractSoloGame) -> {


            accumulatePlayerStats(comparisonStats,
                    abstractSoloGame.getPlayer1().equals(playerId)
                            ? abstractSoloGame.getPlayer1Stats() : abstractSoloGame.getPlayer2Stats(),
                    abstractSoloGame.getWinner().equals(playerId));

        });
    }

    @Override
    public BinaryOperator<PlayerComparisonDto.ComparisonStats> combiner() {
        return (playerComparisonDto, playerComparisonDto2) -> {
            return playerComparisonDto;
        };
    }

    @Override
    public Function<PlayerComparisonDto.ComparisonStats, PlayerComparisonDto.ComparisonStats> finisher() {
        return a -> a;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return new HashSet<>();
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
