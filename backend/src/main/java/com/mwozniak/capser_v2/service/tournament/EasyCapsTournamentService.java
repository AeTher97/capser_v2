package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.tournament.BracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.AbstractSinglesBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsBracketEntry;
import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsTournament;
import com.mwozniak.capser_v2.models.dto.SoloGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.repository.EasyCapsTournamentRepository;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.service.game.EasyCapsGameService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class EasyCapsTournamentService extends AbstractSoloTournamentService<EasyCapsTournament> {


    private final EasyCapsTournamentRepository tournamentRepository;
    private final EasyCapsGameService easyCapsGameService;

    public EasyCapsTournamentService(UserService userService, EasyCapsTournamentRepository tournamentRepository, EasyCapsGameService easyCapsGameService) {
        super(tournamentRepository, userService);
        this.tournamentRepository = tournamentRepository;
        this.easyCapsGameService = easyCapsGameService;
    }


    @Override
    public EasyCapsTournament getTournament(UUID id) throws TournamentNotFoundException {

        Optional<EasyCapsTournament> easyCapsTournamentOptional = tournamentRepository.findEasyCapsTournamentById(id);
        if (easyCapsTournamentOptional.isPresent()) {
            return easyCapsTournamentOptional.get();
        } else {
            throw new TournamentNotFoundException();
        }
    }

    @Transactional
    @Override
    public EasyCapsTournament postGame(UUID tournamentId, UUID entryId, SoloGameDto soloGameDto) throws CapserException {
        EasyCapsTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry> easyCapsBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!easyCapsBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        EasyCapsBracketEntry easyCapsBracketEntry = (EasyCapsBracketEntry) easyCapsBracketEntryOptional.get();

        EasyCapsGame game = createGameObject();
        game.fillCommonProperties(soloGameDto);
        game.validate();
        game.calculateStatsOfAllPlayers();
        EasyCapsGame postedGame = easyCapsGameService.postGameWithoutAcceptance(game);
        easyCapsBracketEntry.setGame(postedGame);
        easyCapsBracketEntry.setFinal(true);
        tournament.resolveAfterGame();
        return tournamentRepository.save(tournament);
    }


    @Override
    protected EasyCapsTournament createTournamentClass() {
        return new EasyCapsTournament();
    }

    @Override
    protected EasyCapsGame createGameObject() {
        return new EasyCapsGame();
    }

    @Override
    protected AbstractSinglesBracketEntry getBracketEntry(UUID tournamentId, UUID entryId) throws TournamentNotFoundException {
        EasyCapsTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry> easyCapsBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!easyCapsBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        return (AbstractSinglesBracketEntry) easyCapsBracketEntryOptional.get();
    }

    protected AbstractSinglesBracketEntry saveBracketEntry(UUID tournamentId, UUID entryId, AbstractSinglesBracketEntry newEntry) throws TournamentNotFoundException {
        EasyCapsTournament tournament = getTournament(tournamentId);
        Optional<BracketEntry> easyCapsBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
        if (!easyCapsBracketEntryOptional.isPresent()) {
            throw new TournamentNotFoundException("Entry not found");
        }
        BracketEntry entry = easyCapsBracketEntryOptional.get();
        BeanUtils.copyProperties(newEntry, entry);
        tournamentRepository.save(tournament);
        return (AbstractSinglesBracketEntry) entry;

    }


}
