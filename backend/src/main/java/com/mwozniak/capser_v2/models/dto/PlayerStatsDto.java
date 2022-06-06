package com.mwozniak.capser_v2.models.dto;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
@Builder
public class PlayerStatsDto {

    @NotNull
    private UUID playerId;

    @NotNull
    private int score;

    @NotNull
    private int sinks;

    private int rebuttals;
}
