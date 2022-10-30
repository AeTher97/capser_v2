package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.enums.Roles;
import com.mwozniak.capser_v2.models.database.PasswordResetToken;
import com.mwozniak.capser_v2.models.database.TeamWithStats;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.dto.*;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.CredentialTakenException;
import com.mwozniak.capser_v2.models.exception.DataValidationException;
import com.mwozniak.capser_v2.models.exception.ResetTokenExpiredException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.models.responses.UserDto;
import com.mwozniak.capser_v2.models.responses.UserMinimized;
import com.mwozniak.capser_v2.repository.PasswordTokenRepository;
import com.mwozniak.capser_v2.repository.UsersRepository;
import com.mwozniak.capser_v2.security.providers.UsernamePasswordProvider;
import com.mwozniak.capser_v2.security.utils.SecurityUtils;
import com.mwozniak.capser_v2.utils.EmailLoader;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Log4j2
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final PasswordTokenRepository passwordTokenRepository;
    @Setter
    private UsernamePasswordProvider usernamePasswordProvider;
    @Setter
    private TeamService teamService;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder, EmailService emailService, PasswordTokenRepository passwordTokenRepository) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.passwordTokenRepository = passwordTokenRepository;
    }

    public User getUser(UUID id) throws UserNotFoundException {
        Optional<User> userOptional = usersRepository.findUserById(id);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public User getUserByEmail(String email) throws UserNotFoundException {
        Optional<User> userOptional = usersRepository.findUserByEmail(email);
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public UserDto getFullUser(UUID id) throws UserNotFoundException {
        Optional<User> userOptional = usersRepository.findUserById(id);
        if (userOptional.isPresent()) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userOptional.get(), userDto);
            List<TeamWithStats> teams = new ArrayList<>();
            userOptional.get().getTeams().forEach(team -> {
                try {
                    teams.add(teamService.findTeam(team));
                } catch (CapserException e) {
                    e.printStackTrace();
                }
            });
            userDto.setTeams(teams);
            return userDto;
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public PlotsDto getUserPlots(UUID id, GameType gameType) throws UserNotFoundException {
        Optional<User> userOptional = usersRepository.findUserById(id);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            switch (gameType) {
                case DOUBLES:
                    return user.getUserDoublesStats().getPlots();
                case SINGLES:
                    return user.getUserSinglesStats().getPlots();
                case EASY_CAPS:
                    return user.getUserEasyStats().getPlots();
                default:
                    return null;
            }
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public Optional<User> getUserOptional(UUID id) {
        return usersRepository.findUserById(id);
    }


    public Optional<User> getUser(String username) {
        return usersRepository.findUserByUsername(username);
    }

    public void saveUser(User user) {
        usersRepository.save(user);
    }

    public void updateLastSeen(User user) {
        user.setLastSeen(new Date());
        usersRepository.save(user);
    }

    public User createUser(CreateUserDto createUserDto) throws CredentialTakenException, NoSuchAlgorithmException, DataValidationException {
        if (usersRepository.findUserByEmail(createUserDto.getEmail()).isPresent()) {
            throw new CredentialTakenException("Email not available");
        } else if (usersRepository.findUserByUsername(createUserDto.getUsername()).isPresent()) {
            throw new CredentialTakenException("Username not available");
        } else if (!createUserDto.getUsername().trim().equals(createUserDto.getUsername())) {
            throw new DataValidationException("Username has leading or trailing spaces");
        }
        User user = User.createUserFromDto(createUserDto, passwordEncoder.encode(createUserDto.getPassword()));
        usersRepository.save(user);
        log.info("User creation successful");
        emailService.sendHtmlMessage(createUserDto.getEmail(), "Welcome to Global Caps League!", EmailLoader.loadRegisteredEmail().replace("${player}", user.getUsername()));
        return user;
    }

    @Transactional
    public UserDto updateUser(UUID id, UpdateUserDto updateUserDto) throws UserNotFoundException, NoSuchAlgorithmException {
        usernamePasswordProvider.checkPassword(updateUserDto.getPassword());
        User user = getUser(id);
        if (updateUserDto.getEmail() != null) {
            if (!usersRepository.findUserByEmail(updateUserDto.getEmail()).isPresent()) {
                try {
                    emailService.sendHtmlMessage(updateUserDto.getEmail(), "Email updated", EmailLoader.loadUpdateEmailEmail().replace("${player}", user.getUsername()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                user.setEmail(updateUserDto.getEmail());
            } else {
                if (!usersRepository.findUserByEmail(updateUserDto.getEmail()).get().getId().equals(id)) {
                    throw new UserNotFoundException("Email taken");
                }
            }
        }
        if (updateUserDto.getUsername() != null) {
            if (!getUser(updateUserDto.getUsername()).isPresent()) {
                try {
                    emailService.sendHtmlMessage(updateUserDto.getEmail(), "Username updated", EmailLoader.loadUpdateUsernameEmail().replace("${player}", updateUserDto.getUsername()));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                user.setUsername(updateUserDto.getUsername());
            } else {
                if (!usersRepository.findUserByUsername(updateUserDto.getUsername()).get().getId().equals(id)) {
                    throw new UserNotFoundException("Username taken");
                }
            }
        }
        User user0 = usersRepository.save(user);

        return getFullUser(user0.getId());
    }

    @Transactional
    public void resetPassword(String email) throws UserNotFoundException {
        User user = getUserByEmail(email);
        String token = UUID.randomUUID().toString();
        createPasswordResetToken(user, token);
        emailService.sendHtmlMessage(email, "Password reset", EmailLoader.loadResetPasswordEmail().replace("${player}", user.getUsername()).replace("${resetCode}", token));
    }

    @Transactional
    public void updatePassword(UpdatePasswordDto updatePasswordDto) throws UserNotFoundException, ResetTokenExpiredException {
        Optional<PasswordResetToken> passwordResetTokenOptional = passwordTokenRepository.findPasswordResetTokenByToken(updatePasswordDto.getCode());
        if (!passwordResetTokenOptional.isPresent()) {
            throw new UserNotFoundException("Reset token invalid");
        }
        PasswordResetToken resetToken = passwordResetTokenOptional.get();
        if (resetToken.isExpired()) {
            throw new ResetTokenExpiredException("Reset token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getPassword()));
        passwordTokenRepository.delete(resetToken);
        usersRepository.save(user);
    }

    @Transactional
    public void changePassword(ChangePasswordDTO changePasswordDTO) throws UserNotFoundException {
        Optional<User> userOptional = usersRepository.findUserById(SecurityUtils.getUserId());
        if(userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
                user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
            }
        } else {
            throw new UserNotFoundException("User with this id not found");
        }
    }


    private void createPasswordResetToken(User user, String token) {
        PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
        passwordTokenRepository.save(passwordResetToken);
    }

    public Page<User> getUsers(Pageable pageable, GameType gameType, int numberOfGamesPlayed) {
        switch (gameType) {
            case EASY_CAPS:
                return usersRepository.findByRoleNotAndUserEasyStatsGamesPlayedGreaterThanEqual(Roles.ADMIN, pageable, numberOfGamesPlayed);
            case UNRANKED:
                return usersRepository.findByRoleNot(Roles.ADMIN, pageable);
            case SINGLES:
                return usersRepository.findByRoleNotAndUserSinglesStatsGamesPlayedGreaterThanEqual(Roles.ADMIN, pageable, numberOfGamesPlayed);
            default:
                return null;
        }
    }

    public Page<UserMinimized> searchUsers(Pageable pageable, String username) {
        Page<User> userPage = usersRepository.findByUsernameContainingIgnoreCase(username, pageable);
        try {
            List<UserMinimized> userMinimizedList = userPage.getContent().stream().filter(user -> !user.getRole().equals(Roles.ADMIN)).map(user -> {
                UserMinimized userMinimized = new UserMinimized();
                BeanUtils.copyProperties(user, userMinimized);
                return userMinimized;
            }).collect(Collectors.toList());
            return new PageImpl<>(userMinimizedList, pageable, userMinimizedList.size());

        } catch (IllegalArgumentException e) {
            return new PageImpl<>(userPage.getContent().stream().map(user -> {
                UserMinimized userMinimized = new UserMinimized();
                BeanUtils.copyProperties(user, userMinimized);
                return userMinimized;
            }).collect(Collectors.toList()), pageable, userPage.getTotalElements());
        }


    }


}
