package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.dto.CreateTournamentDto;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.service.tournament.AbstractTournamentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;


public abstract class AbstractTournamentController<T extends Tournament<?>>{

    private final AbstractTournamentService<T> tournamentService;

    protected AbstractTournamentController(AbstractTournamentService<T> tournamentService) {
        this.tournamentService = tournamentService;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public T createTournament(@Valid @RequestBody CreateTournamentDto createTournamentDto) {
        return tournamentService.createTournament(createTournamentDto);
    }

    @DeleteMapping("/{tournamentId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<?> deleteTournament(@PathVariable UUID tournamentId) throws TournamentNotFoundException {
        tournamentService.deleteTournament(tournamentId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tournamentId}")
    public T getTournament(@PathVariable UUID tournamentId) throws TournamentNotFoundException {
        return tournamentService.getTournament(tournamentId);
    }

    @GetMapping
    public ResponseEntity<Page<T>> getTournaments(@RequestParam int pageSize, @RequestParam int pageNumber) {
        return ResponseEntity.ok().body(tournamentService.getTournaments(PageRequest.of(pageNumber, pageSize, Sort.by("date").descending())));
    }


    @PostMapping("/{tournamentId}/seed")
    @PreAuthorize("hasAuthority('ADMIN')")
    public T seedPlayers(@PathVariable UUID tournamentId) throws TournamentNotFoundException {
        return tournamentService.seedPlayers(tournamentId);
    }
}
