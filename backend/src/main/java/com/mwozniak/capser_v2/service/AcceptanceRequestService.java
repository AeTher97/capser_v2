package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.responses.AcceptanceRequestWithAGame;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import com.mwozniak.capser_v2.service.game.*;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AcceptanceRequestService {

    private final AcceptanceRequestRepository acceptanceRequestRepository;

    private final List<GameService> gameServiceList;

    public AcceptanceRequestService(AcceptanceRequestRepository acceptanceRequestRepository, DoublesService doublesService, EasyCapsGameService easyCapsGameService, SinglesGameService singlesGameService, UnrankedGameService unrankedGameService) {
        this.acceptanceRequestRepository = acceptanceRequestRepository;
        gameServiceList = new ArrayList<>();
        gameServiceList.add(doublesService);
        gameServiceList.add(easyCapsGameService);
        gameServiceList.add(singlesGameService);
        gameServiceList.add(unrankedGameService);
    }

    @Transactional
    public List<AcceptanceRequestWithAGame> getAcceptanceRequests(){
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByAcceptingUser(SecurityUtils.getUserId());
        return acceptanceRequestList.stream().map(acceptanceRequest -> {
                    AcceptanceRequestWithAGame acceptanceRequestWithAGame = new AcceptanceRequestWithAGame();
                    acceptanceRequestWithAGame.setAcceptanceRequest(acceptanceRequest);
                    Optional<GameService> gameService = gameServiceList.stream().filter(gameService1 -> gameService1.getGameType().equals(acceptanceRequest.getGameType())).findAny();
                    try {
                        acceptanceRequestWithAGame.setGame(gameService.get().findGame(acceptanceRequest.getGameToAccept()));
                    } catch (CapserException e) {
                        e.printStackTrace();
                    }
                    return acceptanceRequestWithAGame;
        }).collect(Collectors.toList());
    }
}
