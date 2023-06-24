package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.singles.UnrankedBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.UnrankedTournament;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.service.tournament.UnrankedTournamentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/unranked/tournaments")
public class UnrankedTournamentController extends AbstractSinglesTournamentController<UnrankedTournament> {
    public UnrankedTournamentController(UnrankedTournamentService unrankedTournamentService) {
        super(unrankedTournamentService);
    }

    @PostMapping("/{tournamentId}/setSeed")
    @PreAuthorize("hasAuthority('ADMIN')")
    public UnrankedTournament setSeeds(@PathVariable UUID tournamentId, @RequestBody List<UnrankedBracketEntry> bracketEntries) throws TournamentNotFoundException {
        return doSetSeeds(tournamentId, bracketEntries);
    }
}
