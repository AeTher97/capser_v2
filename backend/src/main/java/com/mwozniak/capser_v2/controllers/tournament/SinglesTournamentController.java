package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.singles.SinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.SinglesTournament;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.service.tournament.SoloTournamentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/singles/tournaments")
public class SinglesTournamentController extends AbstractSinglesTournamentController<SinglesTournament> {
    public SinglesTournamentController(SoloTournamentService singlesTournamentService) {
        super(singlesTournamentService);
    }

    @PostMapping("/{tournamentId}/setSeed")
    @PreAuthorize("hasAuthority('ADMIN')")
    public SinglesTournament setSeeds(@PathVariable UUID tournamentId, @RequestBody List<SinglesBracketEntry> bracketEntries) throws TournamentNotFoundException {
        return doSetSeeds(tournamentId, bracketEntries);
    }
}
