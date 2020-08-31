package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.game.GameEventEntity;
import lombok.Data;

import java.util.List;

@Data
public abstract class AbstractGameDto {

    private List<GameEventEntity> gameEventList;

    private GameMode gameMode;
}
