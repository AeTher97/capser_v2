package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.EasyCapsTournament;
import com.mwozniak.capser_v2.models.database.tournament.SinglesTournament;
import com.mwozniak.capser_v2.service.tournament.EasyCapsTournamentService;
import com.mwozniak.capser_v2.service.tournament.SinglesTournamentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/easy/tournaments")
public class EasyCapsTournamentController extends AbstractSinglesTournamentController<EasyCapsTournament>{
    public EasyCapsTournamentController(EasyCapsTournamentService easyCapsTournamentService) {
        super(easyCapsTournamentService);
    }
}
