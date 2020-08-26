package com.mwozniak.capser_v2.utils;


import com.mwozniak.capser_v2.models.database.User;

import java.util.List;

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
    public static void calculate(List<User> players,
                                       int k, boolean d) {

        if (players.size() < 2) {
            System.out.println("Not enough players in the list");
            throw new RuntimeException("Weird");
        }

        User player1 = players.get(0);
        User player2 = players.get(1);

        float player2Probability = probability(player1.getUserSinglesStats().getPoints(), player2.getUserSinglesStats().getPoints());
        float player1Probability = probability(player2.getUserSinglesStats().getPoints(), player1.getUserSinglesStats().getPoints());

        if (d) {
            players.get(0).getUserSinglesStats().setPoints(player1.getUserSinglesStats().getPoints() + k * (1 - player1Probability));
            players.get(1).getUserSinglesStats().setPoints(player2.getUserSinglesStats().getPoints() + k * (0 - player2Probability));
        } else {
            players.get(0).getUserSinglesStats().setPoints(player1.getUserSinglesStats().getPoints() + k * (0 - player1Probability));
            players.get(1).getUserSinglesStats().setPoints(player2.getUserSinglesStats().getPoints() + k * (1 - player2Probability));
        }


    }

}
