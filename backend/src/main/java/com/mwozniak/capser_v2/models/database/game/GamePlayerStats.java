package com.mwozniak.capser_v2.models.database.game;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "game_stats")
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    private float beersDowned;
    private boolean nakedLap;

}
