package com.mwozniak.capser_v2.models.database;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
public class UserStats {

    @Id
    @Column(name = "id")
    private UUID id;

    @OneToOne
    @MapsId
    private User user;

    private float points;

    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;

    private int beersDowned;

    private int totalPointsMade;
    private int totalPointsLost;

    private int totalSinksMade;
    private int totalSinksLost;
    private int nakedLaps;

    private float avgRebuttals;
    private float winLossRatio;
    private float sinksMadeLostRatio;
    private float pointsMadeLostRatio;


}
