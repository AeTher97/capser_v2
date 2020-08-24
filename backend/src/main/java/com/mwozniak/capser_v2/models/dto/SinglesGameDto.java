package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.GameEventEntity;
import com.mwozniak.capser_v2.models.database.GamePlayerStats;
import lombok.Data;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class SinglesGameDto {

    private UUID player1;
    private UUID player2;

    private GameMode gameMode;

    private PlayerStatsDto player1Stats;

    private PlayerStatsDto player2Stats;

    private List<GameEventEntity> gameEventList;
}
