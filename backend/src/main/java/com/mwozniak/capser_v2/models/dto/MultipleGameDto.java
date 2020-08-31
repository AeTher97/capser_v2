package com.mwozniak.capser_v2.models.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class MultipleGameDto extends AbstractGameDto {

    private List<PlayerStatsDto> playerStatsDtos;

    private UUID team1;

    private UUID team2;

    private List<UUID> team1Players;

    private List<UUID> team2Players;


    private int team1Score;
    private int team2Score;

}
