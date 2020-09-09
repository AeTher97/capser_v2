package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class SinglesGameDto extends AbstractGameDto {


    @NotNull
    private PlayerStatsDto player1Stats;

    @NotNull
    private PlayerStatsDto player2Stats;

}
