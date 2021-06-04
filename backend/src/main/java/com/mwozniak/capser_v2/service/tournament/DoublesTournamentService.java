package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.multiple.DoublesGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.TeamBridge;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesTournament;
import com.mwozniak.capser_v2.models.dto.MultipleGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.models.exception.UpdatePlayersException;
import com.mwozniak.capser_v2.repository.DoublesTournamentRepository;
import com.mwozniak.capser_v2.service.DoublesService;
import com.mwozniak.capser_v2.service.TeamService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j
@Service
public class DoublesTournamentService extends AbstractTournamentService<DoublesTournament> {

    private final DoublesTournamentRepository doublesTournamentRepository;
    private final DoublesService doublesService;
    private final TeamService teamService;

    public DoublesTournamentService(DoublesTournamentRepository doublesTournamentRepository, DoublesService doublesService, TeamService teamService) {
        super(doublesTournamentRepository);
        this.doublesTournamentRepository = doublesTournamentRepository;
        this.doublesService = doublesService;
        this.teamService = teamService;
    }

    @Transactional
    public DoublesTournament addTeams(UUID id, List<UUID> teams) throws TeamNotFoundException, UpdatePlayersException,TournamentNotFoundException {
        List<TeamBridge> teamObjects = new ArrayList<>();
        List<TeamNotFoundException> errors = new ArrayList<>();
        teams.stream().distinct().forEach(uuid -> {
            try {
                teamObjects.add(new TeamBridge(teamService.getTeam(uuid)));
            } catch (TeamNotFoundException e) {
                errors.add(e);
            }
        });
        if (errors.size() > 0) {
            throw errors.get(0);
        }

        List<UUID> playerIds = new ArrayList<>();
        AtomicBoolean error = new AtomicBoolean(false);
        teamObjects.forEach(teamBridge -> {
            teamBridge.getTeam().getPlayerList().forEach(uuid -> {
                        if (playerIds.contains(uuid)) {
                            error.set(true);
                        } else {
                            playerIds.add(uuid);
                        }
                    }
            );
        });

        if (error.get()) {
            throw new UpdatePlayersException("Cannot add team containing player that is already in the tournament");
        }

        DoublesTournament tournament = getTournament(id);
        tournament.getTeams().clear();
        tournament.getTeams().addAll(teamObjects);
        if (tournament.getTeams().size() > tournament.getSize().getValue()) {
            throw new IllegalStateException("Too many players for this tournament size");
        }
        return doublesTournamentRepository.save(tournament);
    }

    @Transactional
    public DoublesTournament skipGame(UUID tournamentId, UUID entryId, UUID forfeitingId) throws CapserException {
        TeamWithStats forfeitingTeam = teamService.getTeam(forfeitingId);
        DoublesBracketEntry bracketEntry = getBracketEntry(tournamentId, entryId);
        bracketEntry.forfeitGame(forfeitingTeam);
        saveBracketEntry(tournamentId, entryId, bracketEntry);
        DoublesTournament tournament = getTournament(tournamentId);
        tournament.resolveAfterGame();
        return tournament;
    }


    @Override
    protected DoublesTournament createTournamentClass() {
        return new DoublesTournament();
    }

    @Override
    public DoublesTournament getTournament(UUID id) throws TournamentNotFoundException {
        Optional<DoublesTournament> doublesTournamentOptional = doublesTournamentRepository.findDoublesTournamentById(id);
        if (doublesTournamentOptional.isPresent()) {
            return doublesTournamentOptional.get();
        } else {
            throw new TournamentNotFoundException();
        }
    }

    protected DoublesBracketEntry getBracketEntry(UUID tournamentId, UUID entryId) throws TournamentNotFoundException {
        DoublesTournament tournament = getTournament(tournamentId);
        Optional<? extends BracketEntry> singlesBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!singlesBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        return (DoublesBracketEntry) singlesBracketEntryOptional.get();
    }

    protected DoublesBracketEntry saveBracketEntry(UUID tournamentId, UUID entryId, DoublesBracketEntry newEntry) throws TournamentNotFoundException {
        DoublesTournament tournament = getTournament(tournamentId);
        Optional<? extends BracketEntry> easyCapsBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!easyCapsBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        DoublesBracketEntry entry = (DoublesBracketEntry) easyCapsBracketEntryOptional.get();
        BeanUtils.copyProperties(newEntry, entry);
        doublesTournamentRepository.save(tournament);
        return entry;
    }

    public DoublesTournament postGame(UUID tournamentId, UUID entryId, MultipleGameDto multipleGameDto) throws CapserException {
        DoublesTournament tournament = getTournament(tournamentId);
        Optional<? extends BracketEntry> singlesBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!singlesBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        DoublesBracketEntry singlesBracketEntry = (DoublesBracketEntry) singlesBracketEntryOptional.get();

        AbstractGame abstractGame = createGameObject();
        abstractGame.fillCommonProperties(multipleGameDto);
        abstractGame.validateGame();
        abstractGame.calculateGameStats();
        AbstractGame game = doublesService.postGameWithoutAcceptance(abstractGame);
        singlesBracketEntry.setGame((DoublesGame) game);
        singlesBracketEntry.setFinal(true);
        tournament.resolveAfterGame();
        return doublesTournamentRepository.save(tournament);
    }

    private DoublesGame createGameObject() {
        return new DoublesGame();
    }


}
