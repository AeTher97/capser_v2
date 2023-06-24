package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsTournament;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.service.tournament.EasyCapsTournamentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/easy/tournaments")
public class EasyCapsTournamentController extends AbstractSinglesTournamentController<EasyCapsTournament> {
    public EasyCapsTournamentController(EasyCapsTournamentService easyCapsTournamentService) {
        super(easyCapsTournamentService);
    }

    @PostMapping("/{tournamentId}/setSeed")
    @PreAuthorize("hasAuthority('ADMIN')")
    public EasyCapsTournament setSeeds(@PathVariable UUID tournamentId, @RequestBody List<EasyCapsBracketEntry> bracketEntries) throws TournamentNotFoundException {
        return doSetSeeds(tournamentId, bracketEntries);
    }
}
