package com.mwozniak.capser_v2.controllers.team;

import com.mwozniak.capser_v2.models.dto.CreateTeamDto;
import com.mwozniak.capser_v2.models.exception.TeamNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.service.TeamService;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/teams")
@Log4j2
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.isPresentInTeam(#createTeamDto)")
    public ResponseEntity<Object> addTeam(@Valid @RequestBody CreateTeamDto createTeamDto) throws UserNotFoundException {
        log.info("Creating team " + createTeamDto.getName());
        return ResponseEntity.ok(teamService.createTeam(createTeamDto));
    }

    @GetMapping
    public ResponseEntity<Object> getTeams(@RequestParam int pageSize, @RequestParam int pageNumber) {
        log.info("Listing teams");
        return ResponseEntity.ok().body(teamService.getTeams(PageRequest.of(pageNumber, pageSize, Sort.by("doublesStats.points").descending()), 5));
    }

    @PutMapping("/{teamId}")
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.hasAccessToTeam(#teamId)")
    public ResponseEntity<Object> updateTeam(@PathVariable UUID teamId, @RequestBody @Valid CreateTeamDto createTeamDto) throws TeamNotFoundException {
        log.info("Updaing " + teamId.toString() + " team information");
        return ResponseEntity.ok(teamService.updateTeam(teamId, createTeamDto));
    }

    @GetMapping("/{userId}")
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.hasAccessToUser(#userId)")
    public ResponseEntity<Object> getPlayerTeams(@RequestParam int pageSize, @RequestParam int pageNumber, @PathVariable UUID userId) {
        log.info("Getting " + userId.toString() + " teams");
        return ResponseEntity.ok().body(teamService.getUserTeams(PageRequest.of(pageNumber, pageSize), userId));
    }

    @DeleteMapping("/{teamId}")
    @PreAuthorize("hasAuthority('USER') and @accessVerificationBean.hasAccessToTeam(#teamId)")
    public ResponseEntity<Object> deleteTeam(@PathVariable UUID teamId) throws TeamNotFoundException {
        log.info("Deleting " + teamId.toString() + "teams");
        teamService.deleteTeam(teamId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchTeams(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam String username) {
        return ResponseEntity.ok(teamService.searchTeams(PageRequest.of(pageNumber, pageSize), username));
    }

    @GetMapping("/name/{teamId}")
    public ResponseEntity<Object> getTeam(@PathVariable UUID teamId) throws TeamNotFoundException {
        log.info("Getting " + teamId.toString() + " team info");
        return ResponseEntity.ok(teamService.getTeam(teamId));
    }


    @GetMapping("/name/{teamId}/full")
    public ResponseEntity<Object> getFullTeam(@PathVariable UUID teamId) throws TeamNotFoundException {
        log.info("Getting " + teamId.toString() + " team info");
        return ResponseEntity.ok(teamService.getFullTeam(teamId));
    }


}
