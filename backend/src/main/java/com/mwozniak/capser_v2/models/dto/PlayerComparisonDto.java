package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.models.responses.UserMinimized;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlayerComparisonDto {

    private UserMinimized player1;
    private UserMinimized player2;
    private int gamesPlayed;
    private ComparisonStats player1Stats = new ComparisonStats();
    private ComparisonStats player2Stats = new ComparisonStats();


    @Data
    public static class ComparisonStats {
        private int gamesWon;
        private int gamesLost;
        private int pointsMade;
        private int rebuttalsMade;
        private int sinksMade;
        private double pointsChange;
        private int nakedLaps;
        private float beersDowned;
    }
}
