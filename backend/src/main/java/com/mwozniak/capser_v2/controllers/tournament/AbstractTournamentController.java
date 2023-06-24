package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.dto.CreateTournamentDto;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.service.tournament.AbstractTournamentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


@Log4j2
public abstract class AbstractTournamentController<T extends Tournament> {

    private final AbstractTournamentService<T> tournamentService;

    protected AbstractTournamentController(AbstractTournamentService<T> tournamentService) {
        this.tournamentService = tournamentService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public T createTournament(@Valid @RequestBody CreateTournamentDto createTournamentDto) {
        log.info("Creating tournament " + createTournamentDto.getTournamentName());
        return tournamentService.createTournament(createTournamentDto);
    }

    @DeleteMapping("/{tournamentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteTournament(@PathVariable UUID tournamentId) throws TournamentNotFoundException {
        log.info("Deleting tournament " + tournamentId.toString());
        tournamentService.deleteTournament(tournamentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tournamentId}")
    public T getTournament(@PathVariable UUID tournamentId) throws TournamentNotFoundException {
        log.info("Getting detailed " + tournamentId.toString() + " tournment info");
        return tournamentService.getTournament(tournamentId);
    }

    @GetMapping
    public ResponseEntity<Page<T>> getTournaments(@RequestParam int pageSize, @RequestParam int pageNumber) {
        log.info("Getting list of tournaments");
        return ResponseEntity.ok().body(tournamentService.getTournaments(PageRequest.of(pageNumber, pageSize, Sort.by("date").descending())));
    }


    @PostMapping("/{tournamentId}/seed")
    @PreAuthorize("hasAuthority('ADMIN')")
    public T seedPlayers(@PathVariable UUID tournamentId) throws TournamentNotFoundException {
        log.info("Seeding players in " + tournamentId.toString());
        return tournamentService.seedPlayers(tournamentId);
    }

    protected T doSetSeeds(@PathVariable UUID tournamentId, @RequestBody List<? extends BracketEntry> bracketEntries) throws TournamentNotFoundException {
        log.info("Setting seeds in " + tournamentId.toString());
        return tournamentService.setSeeds(tournamentId, bracketEntries);
    }
}
