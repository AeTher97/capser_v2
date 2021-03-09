package com.mwozniak.capser_v2.controllers.users;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.dto.CreateUserDto;
import com.mwozniak.capser_v2.models.dto.UpdateUserDto;
import com.mwozniak.capser_v2.models.exception.CredentialTakenException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.models.responses.UserMinimized;
import com.mwozniak.capser_v2.service.UserService;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@Log4j
public class UsersController {

    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<Object> getUsers(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam GameType gameType) {
        return ResponseEntity.ok(userService.getUsers(PageRequest.of(pageNumber, pageSize, Sort.by(getSortString(gameType)).descending())));
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchUsers(@RequestParam int pageSize, @RequestParam int pageNumber, @RequestParam String username) {
        return ResponseEntity.ok(userService.searchUsers(PageRequest.of(pageNumber, pageSize), username));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUser(@PathVariable UUID userId) throws UserNotFoundException {
        UserMinimized userMinimized = new UserMinimized();
        BeanUtils.copyProperties(userService.getUser(userId), userMinimized);
        return ResponseEntity.ok(userMinimized);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid CreateUserDto createUserDto) throws CredentialTakenException {
        log.info("Creating user");
        return ResponseEntity.ok(userService.createUser(createUserDto));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@RequestBody UpdateUserDto updateUserDto, @PathVariable UUID userId) throws UserNotFoundException {
        return ResponseEntity.ok(userService.updateUser(userId, updateUserDto));
    }


    private String getSortString(GameType gameType) {
        switch (gameType) {
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
