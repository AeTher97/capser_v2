package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.EasyCapsGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.EasyCapsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EasyCapsGameService  extends AbstractGameService{

    private final EasyCapsRepository easyCapsRepository;

    @Override
    public AcceptanceRequestType getAcceptanceRequestType() {
        return AcceptanceRequestType.EASY;
    }


    public EasyCapsGameService(AcceptanceRequestRepository acceptanceRequestRepository, UserService userService, NotificationService notificationService, EasyCapsRepository easyCapsRepository) {
        super(acceptanceRequestRepository, userService, notificationService);
        this.easyCapsRepository = easyCapsRepository;
    }

    @Override
    public AbstractGame saveGame(AbstractGame abstractGame) {
        return easyCapsRepository.save((EasyCapsGame)abstractGame);
    }


    @Override
    public EasyCapsGame findGame(UUID uuid) throws CapserException {
        Optional<EasyCapsGame> singlesGameOptional = easyCapsRepository.findEasyCapsGameById(uuid);
        if (singlesGameOptional.isPresent()) {
            EasyCapsGame game = singlesGameOptional.get();
            if (!game.getGameType().equals(GameType.EASY_CAPS)) {
                throw new GameNotFoundException("This easy caps game doesn't exist");
            } else {
                return game;
            }
        } else {
            throw new GameNotFoundException("This game doesn't exist");
        }
    }
    @Override
    public List<AbstractGame> listGames() {
        return (List<AbstractGame>)(List<?>)easyCapsRepository.findAll();
    }

    @Override
    public List<AbstractGame> listGames(Pageable pageable) {
        return (List<AbstractGame>)(List<?>)easyCapsRepository.findAll(pageable);
    }



}
