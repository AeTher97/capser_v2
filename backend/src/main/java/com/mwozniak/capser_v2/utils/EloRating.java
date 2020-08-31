package com.mwozniak.capser_v2.utils;


import lombok.AllArgsConstructor;
import lombok.Data;

public class EloRating {

    static float probability(float rating1,
                             float rating2) {
        return 1.0f * 1.0f / (1 + 1.0f *
                (float) (Math.pow(10, 1.0f *
                        (rating1 - rating2) / 400)));
    }

    // Function to calculate Elo rating
    // K is a constant.
    // d determines whether Player A wins
    // or Player B.
    public static EloResult calculate(float rating1, float rating2,
                                      int k, boolean d) {


        float player2Probability = probability(rating1, rating2);
        float player1Probability = probability(rating2, rating1);

        if (d) {
            return new EloResult(k * (1 - player1Probability), k * (0 - player2Probability));
        } else {
            return new EloResult(k * (0 - player1Probability), k * (1 - player2Probability));
        }

    }

    @Data
    @AllArgsConstructor
    public static class EloResult {
        float result1;
        float result2;
    }

}
