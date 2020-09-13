package com.mwozniak.capser_v2.models.responses;

import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import lombok.Data;

@Data
public class AcceptanceRequestWithAGame {
    private AcceptanceRequest acceptanceRequest;
    private AbstractGame abstractGame;
}
