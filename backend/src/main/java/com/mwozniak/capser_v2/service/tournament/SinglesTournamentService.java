package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.SinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.SinglesTournament;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.repository.SinglesTournamentRepository;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.service.game.SinglesGameService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class SinglesTournamentService  extends AbstractSinglesTournamentService<SinglesTournament> {

    private final SinglesTournamentRepository tournamentRepository;
    private final SinglesGameService singlesGameService;

    public SinglesTournamentService(UserService userService, SinglesTournamentRepository tournamentRepository, SinglesGameService singlesGameService) {
        super(tournamentRepository,userService);
        this.tournamentRepository = tournamentRepository;
        this.singlesGameService = singlesGameService;
    }

    @Override
    public SinglesTournament getTournament(UUID id) throws TournamentNotFoundException {

        Optional<SinglesTournament> singlesTournamentOptional = tournamentRepository.findSinglesTournamentById(id);
        if(singlesTournamentOptional.isPresent()){
            return singlesTournamentOptional.get();
        } else {
            throw new TournamentNotFoundException();
        }
    }

    @Transactional
    @Override
    public SinglesTournament postGame(UUID tournamentId, UUID entryId, SinglesGameDto singlesGameDto) throws CapserException {
        SinglesTournament tournament = getTournament(tournamentId);
        Optional<? extends AbstractSinglesBracketEntry> singlesBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!singlesBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        SinglesBracketEntry singlesBracketEntry = (SinglesBracketEntry) singlesBracketEntryOptional.get();

        AbstractGame abstractGame = createGameObject();
        abstractGame.fillCommonProperties(singlesGameDto);
        abstractGame.validateGame();
        abstractGame.calculateGameStats();
        AbstractGame game = singlesGameService.postGameWithoutAcceptance(abstractGame);
        singlesBracketEntry.setGame((SinglesGame) game);
        singlesBracketEntry.setFinal(true);
        tournament.resolveAfterGame();
        return tournamentRepository.save(tournament);
    }

    @Override
    protected SinglesTournament createTournamentClass() {
        return new SinglesTournament();
    }

    @Override
    protected AbstractGame createGameObject() {
        return new SinglesGame();
    }

    @Override
    protected AbstractSinglesBracketEntry getBracketEntry(UUID tournamentId, UUID entryId) throws TournamentNotFoundException {
        SinglesTournament tournament = getTournament(tournamentId);
        Optional<? extends AbstractSinglesBracketEntry> singlesBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!singlesBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        return singlesBracketEntryOptional.get();
    }

    protected AbstractSinglesBracketEntry saveBracketEntry(UUID tournamentId, UUID entryId, AbstractSinglesBracketEntry newEntry) throws TournamentNotFoundException {
        SinglesTournament tournament = getTournament(tournamentId);
        Optional<? extends AbstractSinglesBracketEntry> easyCapsBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!easyCapsBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        AbstractSinglesBracketEntry entry = easyCapsBracketEntryOptional.get();
        BeanUtils.copyProperties(newEntry, entry);
        tournamentRepository.save(tournament);
        return entry;

    }

}
