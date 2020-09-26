package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.UserStats;
import com.mwozniak.capser_v2.models.dto.CreateTeamDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.TeamRepository;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

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
        if (teamWithStatsOptional.isPresent()) {
            return teamWithStatsOptional.get();
        } else {
            throw new TeamNotFoundException("Team not found");
        }
    }

    @Transactional
    public TeamWithStats createTeam(CreateTeamDto createTeamDto) throws UserNotFoundException {
        TeamWithStats teamWithStats = new TeamWithStats();
        teamWithStats.setDoublesStats(new UserStats());
        teamWithStats.setPlayerList(createTeamDto.getPlayers());
        teamWithStats.setName(createTeamDto.getName());
        teamWithStats.setActive(true);
        TeamWithStats saved = teamRepository.save(teamWithStats);

        for (UUID id : createTeamDto.getPlayers()) {
            User user = userService.getUser(id);
            user.getTeams().add(saved.getId());
            userService.saveUser(user);
        }

        return saved;
    }

    public Page<TeamWithStats> getTeams(Pageable pageable) {
        return teamRepository.findAll(pageable);
    }


    public Page<TeamWithStats> getUserTeams(Pageable pageable, UUID userId) {
        return teamRepository.findByPlayerListContaining(pageable, userId);
    }

    public void deleteTeam(UUID id) throws TeamNotFoundException {
        Optional<TeamWithStats> teamWithStatsOptional = teamRepository.findTeamById(id);
        if (teamWithStatsOptional.isPresent()) {
            TeamWithStats teamWithStats = teamWithStatsOptional.get();
            teamWithStats.setActive(false);
            teamRepository.save(teamWithStats);
        } else {
            throw new TeamNotFoundException("No team whit this id");
        }
    }

    public TeamWithStats updateTeam(UUID id, CreateTeamDto createTeamDto) throws TeamNotFoundException {
        Optional<TeamWithStats> teamWithStatsOptional = teamRepository.findTeamById(id);
        if (teamWithStatsOptional.isPresent()) {
            TeamWithStats teamWithStats = teamWithStatsOptional.get();
            teamWithStats.setName(createTeamDto.getName());
            teamWithStats.setPlayerList(createTeamDto.getPlayers());
            return teamRepository.save(teamWithStats);
        } else {
            throw new TeamNotFoundException("No team whit this id");
        }
    }

    public Page<TeamWithStats> searchTeams(Pageable pageable, String teamName) {
        UUID userId = SecurityUtils.getUserId();
        Page<TeamWithStats> teamPage = teamRepository.findByNameContainingAndPlayerListNotContainingAndActive(pageable, teamName, userId, true);

        List<TeamWithStats> teamList = teamPage.getContent()
                .stream().filter(team -> !team.getPlayerList().contains(userId)).collect(Collectors.toList());
        return new PageImpl<>(teamList, pageable, teamList.size());

    }

    public TeamWithStats getTeam(UUID teamId) throws TeamNotFoundException {
        Optional<TeamWithStats> teamWithStatsOptional = teamRepository.findTeamById(teamId);
        if (teamWithStatsOptional.isPresent()) {
            return teamWithStatsOptional.get();
        } else {
            throw new TeamNotFoundException("Team with this id not found");
        }
    }

}
