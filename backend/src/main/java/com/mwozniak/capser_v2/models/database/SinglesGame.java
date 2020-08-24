package com.mwozniak.capser_v2.models.database;

import com.mwozniak.capser_v2.enums.GameEvent;
import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.enums.GameType;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Table(name = "singles_games")
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SinglesGame {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    private static final GameType gameType= GameType.SINGLES;

    @Temporal(TemporalType.TIMESTAMP)
    private Date time;

    @Setter
    private UUID player1;
    @Setter
    private UUID player2;

    private UUID winner;

    @Setter
    private GameMode gameMode;

    @Setter
    private boolean nakedLap;


    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_1_stats_id",referencedColumnName = "id",nullable = false)
    private GamePlayerStats player1Stats;
    @OneToOne(cascade = CascadeType.ALL,orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "player_2_stats_id",referencedColumnName = "id",nullable = false)
    private GamePlayerStats player2Stats;

    @Setter
    @OneToMany( fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GameEventEntity> gameEventList;
}
