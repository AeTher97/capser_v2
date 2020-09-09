package com.mwozniak.capser_v2.controllers.users;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.service.UserService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Object> getUsers(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam GameType gameType){
        return ResponseEntity.ok(userService.getUsers(PageRequest.of(pageNumber,pageSize, Sort.by(getSortString(gameType)).descending())));
    }

    private String getSortString(GameType gameType){
        switch (gameType){
            case DOUBLES:
                return "userDoublesStats.points";
            case SINGLES:
                return "userSinglesStats.points";
            case UNRANKED:
                return "userUnrankedStats.points";
            case EASY_CAPS:
                return "userEasyStats.points";
            default:
                return "";
        }
    }
}
