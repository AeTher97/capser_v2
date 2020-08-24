package com.mwozniak.capser_v2.models.database;


import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "game_stats")
public class GamePlayerStats {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private UUID playerId;

    private int score;
    private int sinks;
    private int rebuttals;
    private float pointsChange;
    private int beersDowned;
    private boolean nakedLap;

}
