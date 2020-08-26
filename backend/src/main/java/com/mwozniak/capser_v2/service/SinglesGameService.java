package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.SinglesGame;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.SinglesRepository;
import com.mwozniak.capser_v2.repository.UsersRepository;
import com.mwozniak.capser_v2.utils.EloRating;
import org.hibernate.cache.CacheException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SinglesGameService extends AbstractGameService {

    private final SinglesRepository singlesRepository;

    public SinglesGameService(SinglesRepository singlesRepository,
                              AcceptanceRequestRepository acceptanceRequestRepository,
                              UserService userService,
                              NotificationService notificationService) {
        super(acceptanceRequestRepository, userService, notificationService);
        this.singlesRepository =singlesRepository;
    }

    @Override
    public AbstractGame saveGame(AbstractGame abstractGame) {
        return singlesRepository.save((SinglesGame)abstractGame);
    }


    @Override
    public SinglesGame findGame(UUID uuid) throws CapserException {
        Optional<SinglesGame> singlesGameOptional = singlesRepository.findSinglesGameById(uuid);
        if (singlesGameOptional.isPresent()) {
            SinglesGame game = singlesGameOptional.get();
            if (!game.getGameType().equals(GameType.SINGLES)) {
                throw new GameNotFoundException("This singles game doesn't exist");
            } else {
                return game;
            }
        } else {
            throw new GameNotFoundException("This game doesn't exist");
        }
    }


}
