package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.UnrankedRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UnrankedGameService extends AbstractGameService {

    private final UnrankedRepository unrankedRepository;

    public UnrankedGameService(AcceptanceRequestRepository acceptanceRequestRepository,
                               UserService userService,
                               NotificationService notificationService,
                               UnrankedRepository unrankedRepository) {
        super(acceptanceRequestRepository, userService, notificationService);
        this.unrankedRepository = unrankedRepository;
    }

    @Override
    public AbstractGame saveGame(AbstractGame abstractGame) {
        return unrankedRepository.save((UnrankedGame) abstractGame);
    }

    @Override
    public UnrankedGame findGame(UUID uuid) throws CapserException {
        Optional<UnrankedGame> unrankedGameOptional = unrankedRepository.findUnrankedGameById(uuid);
        if (unrankedGameOptional.isPresent()) {
            return unrankedGameOptional.get();
        } else {
            throw new GameNotFoundException("This unranked game doesn't exist");
        }
    }

    @Override
    public List<AbstractGame> listGames() {
        return (List<AbstractGame>) (List<?>) unrankedRepository.findAll();
    }

    @Override
    public Page<AbstractGame> listGames(Pageable pageable) {
        return (Page<AbstractGame>)(Page<?>) unrankedRepository.findAll(pageable);
    }

    @Override
    public AcceptanceRequestType getAcceptanceRequestType() {
        return AcceptanceRequestType.UNRANKED;
    }

    @Override
    public GameType getGameType() {
        return GameType.UNRANKED;
    }
}
