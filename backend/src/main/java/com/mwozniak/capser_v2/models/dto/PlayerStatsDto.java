package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PlayerStatsDto {

    private UUID playerId;

    private int score;
    private int sinks;
}
