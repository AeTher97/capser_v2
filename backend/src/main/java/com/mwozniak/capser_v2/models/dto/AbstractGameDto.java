package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.game.GameEventEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractGameDto {

    private List<GameEventEntity> gameEvents;

    @NotNull
    private GameMode gameMode;
}
