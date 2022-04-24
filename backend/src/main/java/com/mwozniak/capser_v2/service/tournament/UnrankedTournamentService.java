package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.UnrankedBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.UnrankedTournament;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.repository.UnrankedTournamentRepository;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.service.game.UnrankedGameService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UnrankedTournamentService extends AbstractSinglesTournamentService<UnrankedTournament> {

    private final UnrankedTournamentRepository tournamentRepository;
    private final UnrankedGameService unrankedGameService;

    public UnrankedTournamentService(UserService userService, UnrankedTournamentRepository tournamentRepository, UnrankedGameService unrankedGameService) {
        super(tournamentRepository, userService);
        this.tournamentRepository = tournamentRepository;
        this.unrankedGameService = unrankedGameService;
    }

    @Override
    public UnrankedTournament getTournament(UUID id) throws TournamentNotFoundException {

        Optional<UnrankedTournament> unrankedTournamentOptional = tournamentRepository.findUnrankedTournamentById(id);
        if (unrankedTournamentOptional.isPresent()) {
            return unrankedTournamentOptional.get();
        } else {
            throw new TournamentNotFoundException();
        }
    }

    @Transactional
    @Override
    public UnrankedTournament postGame(UUID tournamentId, UUID entryId, SinglesGameDto singlesGameDto) throws CapserException {
        UnrankedTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry> unrankedBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!unrankedBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        UnrankedBracketEntry unrankedBracketEntry = (UnrankedBracketEntry) unrankedBracketEntryOptional.get();

        AbstractGame abstractGame = createGameObject();
        abstractGame.fillCommonProperties(singlesGameDto);
        abstractGame.validateGame();
        abstractGame.calculateGameStats();
        AbstractGame game = unrankedGameService.postGameWithoutAcceptance(abstractGame);
        unrankedBracketEntry.setGame((UnrankedGame) game);
        unrankedBracketEntry.setFinal(true);
        tournament.resolveAfterGame();
        return tournamentRepository.save(tournament);
    }

    @Override
    protected UnrankedTournament createTournamentClass() {
        return new UnrankedTournament();
    }

    @Override
    protected AbstractGame createGameObject() {
        return new UnrankedGame();
    }

    @Override
    protected AbstractSinglesBracketEntry getBracketEntry(UUID tournamentId, UUID entryId) throws TournamentNotFoundException {
        UnrankedTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry> unrankedBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!unrankedBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        return (AbstractSinglesBracketEntry) unrankedBracketEntryOptional.get();
    }

    protected AbstractSinglesBracketEntry saveBracketEntry(UUID tournamentId, UUID entryId, AbstractSinglesBracketEntry newEntry) throws TournamentNotFoundException {
        UnrankedTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry > easyCapsBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!easyCapsBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        BracketEntry entry = easyCapsBracketEntryOptional.get();
        BeanUtils.copyProperties(newEntry, entry);
        tournamentRepository.save(tournament);
        return (AbstractSinglesBracketEntry) entry;

    }
}
