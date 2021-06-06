package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.multiple.DoublesGame;
import lombok.Data;

@Data
public class DoublesGameDto extends DoublesGame {

    private TeamWithPlayersDto team1Details;
    private TeamWithPlayersDto team2Details;
}
