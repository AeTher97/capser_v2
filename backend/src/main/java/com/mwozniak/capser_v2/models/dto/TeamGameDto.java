package com.mwozniak.capser_v2.models.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
public class TeamGameDto extends AbstractGameDto {

    @NotNull
    private List<PlayerStatsDto> playerStatsDtos;

    @NotNull
    private UUID team1;

    @NotNull
    private UUID team2;

    @NotNull
    private List<UUID> team1Players;

    @NotNull
    private List<UUID> team2Players;

    @NotNull
    private int team1Score;
    @NotNull
    private int team2Score;

}
