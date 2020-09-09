package com.mwozniak.capser_v2.security.utils;

import com.mwozniak.capser_v2.enums.AcceptanceRequestType;
import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import com.mwozniak.capser_v2.models.dto.MultipleGameDto;
import com.mwozniak.capser_v2.models.dto.SinglesGameDto;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@Log4j
public class AccessVerificationBean {

    private AcceptanceRequestRepository acceptanceRequestRepository;

    public boolean hasAccessToUser(String id) {
        return SecurityUtils.getUserId().equals(UUID.fromString(id));
    }

    public boolean canAcceptGame(UUID gameId) {
        List<AcceptanceRequest> acceptanceRequestList = acceptanceRequestRepository.findAcceptanceRequestByGameToAccept(gameId);
        if (acceptanceRequestList.isEmpty()) {
            log.info("Tried to accept non existent game, acces denied");
            return false;
        }

        for (AcceptanceRequest request : acceptanceRequestList) {
            if (request.getAcceptingUser().equals(SecurityUtils.getUserId())) {
                if (request.getAcceptanceRequestType().equals(AcceptanceRequestType.PASSIVE)) {
                    log.info("Tried to own posted game");
                } else {
                    return true;
                }
            }
        }
        log.info("Not votes for acceptance request access, denying");
        return false;
    }

    public boolean isPresentInGameTeamGame(MultipleGameDto multipleGameDto) {
        if (multipleGameDto.getTeam1Players().contains(SecurityUtils.getUserId()) || multipleGameDto.getTeam2Players().contains(SecurityUtils.getUserId())) {
            log.info("Present in game request, access granted");
            return true;
        } else {
            log.info("Not present in game request, access denied");
            return false;
        }
    }

    public boolean isPresentInSinglesGame(SinglesGameDto singlesGameDto) {
        if (singlesGameDto.getPlayer1Stats().getPlayerId().equals(SecurityUtils.getUserId()) || singlesGameDto.getPlayer2Stats().getPlayerId().equals(SecurityUtils.getUserId())) {
            log.info("Present in game request, access granted");
            return true;
        } else {
            log.info("Not present in game request, access denied");
            return false;
        }
    }

}
