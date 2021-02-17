package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.multiple.DoublesGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.DoublesRepository;
import com.mwozniak.capser_v2.service.game.AbstractMultipleGameService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DoublesService extends AbstractMultipleGameService {

    private final DoublesRepository doublesRepository;

    public DoublesService(AcceptanceRequestRepository acceptanceRequestRepository,
                          UserService userService,
                          NotificationService notificationService,
                          TeamService teamService,
                          DoublesRepository doublesRepository) {
        super(acceptanceRequestRepository, userService, notificationService, teamService);
        this.doublesRepository = doublesRepository;
    }

    @Override
    public AbstractGame saveGame(AbstractGame abstractGame) {
        return doublesRepository.save((DoublesGame) abstractGame);
    }

    @Override
    public void removeGame(AbstractGame abstractGame) {
        doublesRepository.delete((DoublesGame) abstractGame);
    }

    @Override
    public AbstractGame findGame(UUID id) throws CapserException {
        Optional<DoublesGame> doublesGameOptional = doublesRepository.findDoublesGameById(id);
        if (doublesGameOptional.isPresent()) {
            return doublesGameOptional.get();
        } else {
            throw new GameNotFoundException("This doubles game doesn't exist");
        }
    }

    @Override
    public List<AbstractGame> listGames() {
        return (List<AbstractGame>) (List<?>) doublesRepository.findAll();
    }

    @Override
    public Page<AbstractGame> listGames(Pageable pageable) {
        return  (Page<AbstractGame>)(Page<?>)doublesRepository.findAll(pageable);
    }

    @Override
    public Page<AbstractGame> listAcceptedGames(Pageable pageable) {
        return  (Page<AbstractGame>)(Page<?>)doublesRepository.findDoublesGameByAcceptedTrue(pageable);
    }

    @Override
    public GameType getGameType() {
        return GameType.DOUBLES;
    }
}
