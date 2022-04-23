package com.mwozniak.capser_v2.models.dto;

import com.mwozniak.capser_v2.models.database.game.team.DoublesGame;
import lombok.Data;

@Data
public class DoublesGameDto extends DoublesGame {

    private TeamWithPlayersDto team1Details;
    private TeamWithPlayersDto team2Details;
}
