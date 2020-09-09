package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class PlayerStatsDto {

    @NotNull
    private UUID playerId;

    @NotNull
    private int score;

    @NotNull
    private int sinks;

    @NotNull
    private int rebuttals;
}
