package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Data
public class SoloGameDto extends AbstractGameDto {


    @NotNull
    @Valid
    private PlayerStatsDto player1Stats;

    @NotNull
    @Valid
    private PlayerStatsDto player2Stats;


}
