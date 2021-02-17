package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.SinglesTournament;
import com.mwozniak.capser_v2.service.tournament.SinglesTournamentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/singles/tournaments")
public class SinglesTournamentController extends AbstractSinglesTournamentController<SinglesTournament> {
    public SinglesTournamentController(SinglesTournamentService singlesTournamentService) {
        super(singlesTournamentService);
    }
}
