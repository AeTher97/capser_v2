package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.UnrankedTournament;
import com.mwozniak.capser_v2.service.tournament.UnrankedTournamentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/unranked/tournaments")
public class UnrankedTournamentController extends AbstractSinglesTournamentController<UnrankedTournament> {
    public UnrankedTournamentController(UnrankedTournamentService unrankedTournamentService) {
        super(unrankedTournamentService);
    }
}
