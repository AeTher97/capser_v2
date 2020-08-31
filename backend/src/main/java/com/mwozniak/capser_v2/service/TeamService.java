package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserService userService;

    public TeamService(TeamRepository teamRepository, UserService userService) {
        this.teamRepository = teamRepository;
        this.userService = userService;
    }

    public TeamWithStats findTeam(UUID id) throws CapserException {
        Optional<TeamWithStats> teamWithStatsOptional = teamRepository.findTeamById(id);
        if(teamWithStatsOptional.isPresent()){
            return teamWithStatsOptional.get();
        } else {
            throw new TeamNotFoundException("Team not found");
        }
    }

    public void createTeam(List<UUID> players) throws UserNotFoundException {
        for(UUID id : players){
            userService.getUser(id);
        }

        TeamWithStats teamWithStats = new TeamWithStats();
        teamWithStats.setDoublesStats(new UserStats());
        teamWithStats.setPlayerList(players);
        teamRepository.save(teamWithStats);
    }

}
