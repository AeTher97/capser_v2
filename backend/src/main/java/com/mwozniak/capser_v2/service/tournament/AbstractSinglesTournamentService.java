package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.database.tournament.AbstractSinglesTournament;
import com.mwozniak.capser_v2.models.database.tournament.SinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.SinglesTournament;
import com.mwozniak.capser_v2.models.database.tournament.UserBridge;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.service.game.AbstractGameService;
import lombok.extern.log4j.Log4j;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j
public abstract class AbstractSinglesTournamentService<T extends AbstractSinglesTournament<?>> extends AbstractTournamentService<T> {

    private final JpaRepository<T, UUID> repository;
    private final AbstractGameService abstractGameService;

    protected AbstractSinglesTournamentService(JpaRepository<T, UUID> repository, UserService userService, AbstractGameService abstractGameService1) {
        super(userService, repository);
        this.repository = repository;
        this.abstractGameService = abstractGameService1;
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

    public abstract T postGame(UUID tournamentId, UUID entryId, SinglesGameDto singlesGameDto) throws CapserException;

    protected abstract AbstractGame createGameObject();


}