package com.mwozniak.capser_v2.controllers.team;

import com.mwozniak.capser_v2.models.dto.CreateTeamDto;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.service.TeamService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.isPresentInTeam(#createTeamDto)")
    public ResponseEntity<Object> addTeam(@Valid @RequestBody CreateTeamDto createTeamDto) throws UserNotFoundException {
        return ResponseEntity.ok(teamService.createTeam(createTeamDto));
    }

    @GetMapping
    public ResponseEntity<Object> getTeams(@RequestParam int pageSize, @RequestParam int pageNumber) {
        return ResponseEntity.ok().body(teamService.getTeams(PageRequest.of(pageNumber, pageSize,Sort.by("doublesStats.points").descending())));
    }

    @PutMapping("/{teamId}")
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.hasAccessToTeam(#teamId)")
    public ResponseEntity<Object> updateTeam(@PathVariable UUID teamId, @RequestBody @Valid CreateTeamDto createTeamDto) throws TeamNotFoundException {
        return ResponseEntity.ok(teamService.updateTeam(teamId, createTeamDto));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.hasAccessToUser(#userId)")
    public ResponseEntity<Object> getPlayerTeams(@RequestParam int pageSize, @RequestParam int pageNumber, @PathVariable UUID userId) {
        return ResponseEntity.ok().body(teamService.getUserTeams(PageRequest.of(pageNumber, pageSize), userId));
    }

    @DeleteMapping("/{teamId}")
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.hasAccessToTeam(#teamId)")
    public ResponseEntity<Object> deleteTeam(@PathVariable UUID teamId) throws TeamNotFoundException {
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchTeams(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam String username) {
        return ResponseEntity.ok(teamService.searchTeams(PageRequest.of(pageNumber, pageSize), username));
    }

    @GetMapping("/name/{teamId}")
    public ResponseEntity<Object> getTeam(@PathVariable UUID teamId) throws TeamNotFoundException {
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }


}
