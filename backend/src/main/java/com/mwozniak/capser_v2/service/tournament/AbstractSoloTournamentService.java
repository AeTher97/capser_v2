package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesTournament;
import com.mwozniak.capser_v2.models.database.tournament.singles.UserBridge;
import com.mwozniak.capser_v2.models.dto.SoloGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
public abstract class AbstractSoloTournamentService<T extends AbstractSinglesTournament> extends AbstractTournamentService<T> {

    private final JpaRepository<T, UUID> repository;
    private final UserService userService;

    protected AbstractSoloTournamentService(JpaRepository<T, UUID> repository, UserService userService) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
    }

    @Transactional
    public T addUsers(UUID id, List<UUID> users) throws UserNotFoundException, TournamentNotFoundException {
        List<UserBridge> userObjects = new ArrayList<>();
        List<UserNotFoundException> errors = new ArrayList<>();
        users.stream().distinct().forEach(uuid -> {
            try {
                userObjects.add(new UserBridge(userService.getUser(uuid)));
            } catch (UserNotFoundException e) {
                errors.add(e);
            }
        });
        if (errors.size() > 0) {
            throw errors.get(0);
        }

        T tournament = getTournament(id);
        tournament.getPlayers().clear();
        tournament.getPlayers().addAll(userObjects);
        if (tournament.getPlayers().size() > tournament.getSize().getValue()) {
            throw new IllegalStateException("Too many players for this tournament size");
        }
        return repository.save(tournament);
    }

    public abstract T postGame(UUID tournamentId, UUID entryId, SoloGameDto soloGameDto) throws CapserException;

    @Transactional
    public T skipGame(UUID tournamentId, UUID entryId, UUID forfeitingId) throws CapserException {
        User forfeitingPlayer = userService.getUser(forfeitingId);
        AbstractSinglesBracketEntry bracketEntry = getBracketEntry(tournamentId, entryId);
        bracketEntry.forfeitGame(forfeitingPlayer);
        saveBracketEntry(tournamentId, entryId, bracketEntry);
        T tournament = getTournament(tournamentId);
        tournament.resolveAfterGame();
        return tournament;
    }

    protected abstract AbstractGame createGameObject();

    protected abstract AbstractSinglesBracketEntry getBracketEntry(UUID tournamentId, UUID entryId) throws TournamentNotFoundException;

    protected abstract AbstractSinglesBracketEntry saveBracketEntry(UUID tournamentId, UUID entryId, AbstractSinglesBracketEntry newEntry) throws TournamentNotFoundException;


}
