package com.mwozniak.capser_v2.controllers.team;

import com.mwozniak.capser_v2.models.dto.CreateTeamDto;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.service.TeamService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER')")
    public void addTeam(@Valid @RequestBody CreateTeamDto createTeamDto) throws UserNotFoundException {
        teamService.createTeam(createTeamDto.getPlayers());
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> getTeams(@RequestParam int pageSize, @RequestParam int pageNumber) {
        return ResponseEntity.ok().body(teamService.getTeams(PageRequest.of(pageNumber, pageSize)));
    }


}
