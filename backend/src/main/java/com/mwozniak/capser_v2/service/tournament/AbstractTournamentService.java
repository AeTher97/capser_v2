package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.tournament.Tournament;
import com.mwozniak.capser_v2.models.dto.CreateTournamentDto;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Data
public abstract class AbstractTournamentService<T extends Tournament<?>> {


    private final JpaRepository<T, UUID> repository;

    public AbstractTournamentService(JpaRepository<T, UUID> repository) {
        this.repository = repository;
    }

    @Transactional
    public T createTournament(CreateTournamentDto createTournamentDto) {
        T tournament = createTournamentClass();
        tournament.setTournamentType(createTournamentDto.getTournamentType());
        tournament.setTournamentName(createTournamentDto.getTournamentName());
        tournament.setSeedType(createTournamentDto.getSeedType());
        tournament.setSize(createTournamentDto.getSize());
        tournament.setOwner(SecurityUtils.getUserId());

        return repository.save(tournament);
    }

    public T seedPlayers(UUID id) throws TournamentNotFoundException {
        Optional<T> tournamentOptional = repository.findById(id);
        if (!tournamentOptional.isPresent()) {
            throw new TournamentNotFoundException();
        }
        T tournament = tournamentOptional.get();
        tournament.seedPlayers();
        return repository.save(tournament);
    }

    public Page<T> getTournaments(PageRequest pageRequest) {
        return repository.findAll(pageRequest);
    }

    @Transactional
    public void deleteTournament(UUID tournamentId) throws TournamentNotFoundException {
        Optional<T> tournament = repository.findById(tournamentId);
        if (tournament.isPresent()) {
            repository.delete(tournament.get());
        } else {
            throw new TournamentNotFoundException("Tournament not found");
        }
    }


    protected abstract T createTournamentClass();

    public abstract T getTournament(UUID id) throws TournamentNotFoundException;


}
