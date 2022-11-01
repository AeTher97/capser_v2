package com.mwozniak.capser_v2.controllers;

import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.dto.SearchItemDto;
import com.mwozniak.capser_v2.models.responses.UserMinimized;
import com.mwozniak.capser_v2.service.TeamService;
import com.mwozniak.capser_v2.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    private final UserService userService;
    private final TeamService teamService;

    public SearchController(UserService userService, TeamService teamService) {
        this.userService = userService;
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<Object> searchDatabase(@RequestParam String searchType, @RequestParam String searchString) {
        List<SearchItemDto> searchItems = new ArrayList<>();

        switch (searchType) {
            case "all":
                searchItems.addAll(mapUsersToSearchItems(userService.searchUsers(PageRequest.of(0, 5), searchString).toList()));
                searchItems.addAll(mapTeamsToSearchItems(teamService.searchTeams(PageRequest.of(0, 5), searchString).toList()));
                break;
            case "player":
                searchItems.addAll(mapUsersToSearchItems(userService.searchUsers(PageRequest.of(0, 10), searchString).toList()));
                break;
            case "team":
                searchItems.addAll(mapTeamsToSearchItems(teamService.searchTeams(PageRequest.of(0, 10), searchString).toList()));
                break;
            default:
                return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(searchItems);
    }


    private List<SearchItemDto> mapUsersToSearchItems(List<UserMinimized> userMinimizedList) {
        return userMinimizedList.stream().map(userMinimized ->
                new SearchItemDto(userMinimized.getId(), userMinimized.getUsername(),
                        SearchItemDto.SearchItemType.PLAYER)).collect(Collectors.toList());
    }

    private List<SearchItemDto> mapTeamsToSearchItems(List<TeamWithStats> teamList) {
        return teamList.stream().map(team ->
                new SearchItemDto(team.getId(), team.getName(),
                        SearchItemDto.SearchItemType.PLAYER)).collect(Collectors.toList());
    }
}
