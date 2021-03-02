package com.mwozniak.capser_v2.controllers.tournament;

import com.mwozniak.capser_v2.models.database.tournament.AbstractSinglesTournament;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.dto.SkipDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.service.tournament.AbstractSinglesTournamentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

public abstract class AbstractSinglesTournamentController<T extends AbstractSinglesTournament<?>> extends AbstractTournamentController<T> {
    private final AbstractSinglesTournamentService<T> tournamentService;

    protected AbstractSinglesTournamentController(AbstractSinglesTournamentService<T> tournamentService) {
        super(tournamentService);
        this.tournamentService = tournamentService;
    }

    @PostMapping("/{tournamentId}/players")
    @PreAuthorize("hasAuthority('ADMIN')")
    public T addUser(@PathVariable UUID tournamentId, @RequestBody List<UUID> users) throws UserNotFoundException, TournamentNotFoundException {
        return tournamentService.addUsers(tournamentId, users);
    }

    ;

    @PostMapping("/{tournamentId}/entry/{entryId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public T addGame(@PathVariable UUID tournamentId, @PathVariable UUID entryId, @Valid @RequestBody SinglesGameDto singlesGameDto) throws CapserException {
        return tournamentService.postGame(tournamentId, entryId, singlesGameDto);
    }

    @PostMapping("/{tournamentId}/entry/{entryId}/skip")
    public T skipGame(@PathVariable UUID tournamentId, @PathVariable UUID entryId, @Valid @RequestBody SkipDto skipDto) throws CapserException {
        return tournamentService.skipGame(tournamentId, entryId, skipDto.getForfeitedId());
    }
}
