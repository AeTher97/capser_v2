package com.mwozniak.capser_v2.models.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mwozniak.capser_v2.models.dto.PlotsDto;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.UUID;

@Table(name = "user_stats")
@Data
@Entity
public class UserStats {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    public UserStats() {
        this.points = 500.0f;
    }


    private float points;

    private int gamesPlayed;
    private int gamesWon;
    private int gamesLost;
    private int gamesLoggedSinks;

    private float beersDowned;

    private int totalRebuttals;

    private int totalPointsMade;
    private int totalPointsLost;

    private int totalSinksMade;
    private int totalSinksLost;
    private int nakedLaps;

    private float avgRebuttals;
    private float winLossRatio;
    private float sinksMadeLostRatio;
    private float pointsMadeLostRatio;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TimeSeries pointsSeries;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    private TimeSeries rebuttalsSeries;

    public PlotsDto getPlots() {
        PlotsDto plotsDto = new PlotsDto();
        plotsDto.setPointSeries(pointsSeries);
        plotsDto.setRebuttalsSeries(rebuttalsSeries);
        return plotsDto;
    }

}
