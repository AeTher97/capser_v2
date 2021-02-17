package com.mwozniak.capser_v2.service.tournament;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.models.database.tournament.*;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TournamentNotFoundException;
import com.mwozniak.capser_v2.repository.SinglesTournamentRepository;
import com.mwozniak.capser_v2.repository.UnrankedTournamentRepository;
import com.mwozniak.capser_v2.service.UnrankedGameService;
import com.mwozniak.capser_v2.service.UserService;
import com.mwozniak.capser_v2.service.game.SinglesGameService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
public class UnrankedTournamentService extends AbstractSinglesTournamentService<UnrankedTournament> {

    private final UnrankedTournamentRepository tournamentRepository;
    private final UnrankedGameService unrankedGameService;

    public UnrankedTournamentService(UserService userService, UnrankedTournamentRepository tournamentRepository, UnrankedGameService unrankedGameService) {
        super(tournamentRepository, userService, unrankedGameService);
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
        Optional<? extends AbstractSinglesBracketEntry> unrankedBracketEntryOptional = tournament.getBracketEntries().stream().filter(singlesBracketEntry1 -> singlesBracketEntry1.getId().equals(entryId)).findAny();
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
}
