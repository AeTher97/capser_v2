package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.game.GameEventEntity;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class SinglesGameDto {

    private GameMode gameMode;

    private PlayerStatsDto player1Stats;

    private PlayerStatsDto player2Stats;

    private List<GameEventEntity> gameEventList;
}
