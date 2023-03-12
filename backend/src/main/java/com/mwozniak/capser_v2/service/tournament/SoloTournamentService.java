package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.SinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.SinglesTournament;
import com.mwozniak.capser_v2.models.dto.SoloGameDto;
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
public class SoloTournamentService extends AbstractSoloTournamentService<SinglesTournament> {

    private final SinglesTournamentRepository tournamentRepository;
    private final SinglesGameService singlesGameService;

    public SoloTournamentService(UserService userService, SinglesTournamentRepository tournamentRepository, SinglesGameService singlesGameService) {
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
    public SinglesTournament postGame(UUID tournamentId, UUID entryId, SoloGameDto soloGameDto) throws CapserException {
        SinglesTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry> singlesBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!singlesBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        SinglesBracketEntry singlesBracketEntry = (SinglesBracketEntry) singlesBracketEntryOptional.get();

        SoloGame game = createGameObject();
        game.fillCommonProperties(soloGameDto);
        game.validate();
        game.calculateStatsOfAllPlayers();
        SoloGame postedGame = singlesGameService.postGameWithoutAcceptance(game);
        singlesBracketEntry.setGame(postedGame);
        singlesBracketEntry.setFinal(true);
        tournament.resolveAfterGame();
        return tournamentRepository.save(tournament);
    }

    @Override
    protected SinglesTournament createTournamentClass() {
        return new SinglesTournament();
    }

    @Override
    protected SoloGame createGameObject() {
        return new SoloGame();
    }

    @Override
    protected AbstractSinglesBracketEntry getBracketEntry(UUID tournamentId, UUID entryId) throws TournamentNotFoundException {
        SinglesTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry> singlesBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!singlesBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        return (AbstractSinglesBracketEntry) singlesBracketEntryOptional.get();
    }

    protected AbstractSinglesBracketEntry saveBracketEntry(UUID tournamentId, UUID entryId, AbstractSinglesBracketEntry newEntry) throws TournamentNotFoundException {
        SinglesTournament tournament = getTournament(tournamentId);
        Optional<  BracketEntry> easyCapsBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!easyCapsBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        BracketEntry entry = easyCapsBracketEntryOptional.get();
        BeanUtils.copyProperties(newEntry, entry);
        tournamentRepository.save(tournament);
        return (AbstractSinglesBracketEntry) entry;

    }

}
