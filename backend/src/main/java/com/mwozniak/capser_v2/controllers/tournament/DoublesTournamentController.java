package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesTournament;
import com.mwozniak.capser_v2.models.dto.MultipleGameDto;
import com.mwozniak.capser_v2.models.dto.SkipDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.models.exception.UpdatePlayersException;
import com.mwozniak.capser_v2.service.tournament.DoublesTournamentService;
import lombok.extern.log4j.Log4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doubles/tournaments")
@Log4j
public class DoublesTournamentController  extends AbstractTournamentController<DoublesTournament> {

    private final DoublesTournamentService doublesTournamentService;

    protected DoublesTournamentController(DoublesTournamentService doublesTournamentService, DoublesTournamentService doublesTournamentService1) {
        super(doublesTournamentService);
        this.doublesTournamentService = doublesTournamentService1;
    }

    @PostMapping("/{tournamentId}/players")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DoublesTournament addUser(@PathVariable UUID tournamentId, @RequestBody List<UUID> teams) throws TeamNotFoundException, TournamentNotFoundException, UpdatePlayersException {
        log.info("Adding users to tournament " + tournamentId.toString());
        return doublesTournamentService.addTeams(tournamentId, teams);
    }


    @PostMapping("/{tournamentId}/entry/{entryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public DoublesTournament addGame(@PathVariable UUID tournamentId, @PathVariable UUID entryId, @Valid @RequestBody MultipleGameDto multipleGameDto) throws CapserException {
        log.info("Posting game in tournament " + tournamentId.toString());
        return doublesTournamentService.postGame(tournamentId, entryId, multipleGameDto);
    }

    @PostMapping("/{tournamentId}/entry/{entryId}/skip")
    public DoublesTournament skipGame(@PathVariable UUID tournamentId, @PathVariable UUID entryId, @Valid @RequestBody SkipDto skipDto) throws CapserException {
        log.info("Skipping game in tournament " + tournamentId.toString());
        return doublesTournamentService.skipGame(tournamentId, entryId, skipDto.getForfeitedId());
    }
}
