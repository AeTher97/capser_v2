package com.mwozniak.capser_v2.models.responses;

import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.game.Game;
import lombok.Data;

@Data
public class AcceptanceRequestWithAGame {
    private AcceptanceRequest acceptanceRequest;
    private Game game;
}
