package com.mwozniak.capser_v2.models.dto;

import lombok.Data;

@Data
public class SinglesGameDto extends AbstractGameDto {

    private PlayerStatsDto player1Stats;

    private PlayerStatsDto player2Stats;

}
