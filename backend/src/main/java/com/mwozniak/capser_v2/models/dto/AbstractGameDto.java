package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.game.GameEventEntity;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public abstract class AbstractGameDto {

    @NotNull
    private List<GameEventEntity> gameEventList;

    @NotNull
    private GameMode gameMode;
}
