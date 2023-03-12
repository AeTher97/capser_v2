package com.mwozniak.capser_v2.service.game;

import com.mwozniak.capser_v2.enums.GameType;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.team.DoublesGame;
import com.mwozniak.capser_v2.models.dto.DoublesGameDto;
import com.mwozniak.capser_v2.models.dto.TeamWithPlayersDto;
import com.mwozniak.capser_v2.models.exception.CapserException;
import com.mwozniak.capser_v2.models.exception.GameNotFoundException;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.AcceptanceRequestRepository;
import com.mwozniak.capser_v2.repository.DoublesRepository;
import com.mwozniak.capser_v2.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DoublesService extends AbstractTeamGameService<DoublesGame> {

    private final DoublesRepository doublesRepository;
    private final TeamService teamService;

    public DoublesService(AcceptanceRequestRepository acceptanceRequestRepository,
                          UserService userService,
                          AchievementService achievementService,
                          NotificationService notificationService,
                          TeamService teamService,
                          EmailService emailService,
                          DoublesRepository doublesRepository, TeamService teamService1) {
        super(achievementService, acceptanceRequestRepository, emailService, userService, notificationService, teamService);
        this.doublesRepository = doublesRepository;
        this.teamService = teamService1;
    }



    @Override
    public DoublesGame saveGame(DoublesGame abstractGame) {
        if (abstractGame instanceof DoublesGameDto) {
            DoublesGame tempObject = doublesRepository.findDoublesGameById(abstractGame.getId()).get();
            BeanUtils.copyProperties(abstractGame, tempObject);
            if (abstractGame.isAccepted()) {
                tempObject.setAccepted();
            }
            return doublesRepository.save(tempObject);
        }
        return doublesRepository.save(abstractGame);
    }

    @Override
    public void removeGame(DoublesGame abstractGame) {
        doublesRepository.delete((DoublesGame) abstractGame);
    }

    @Override
    public DoublesGame findGame(UUID id) throws CapserException {
        Optional<DoublesGame> doublesGameOptional = doublesRepository.findDoublesGameById(id);
        if (doublesGameOptional.isPresent()) {
            DoublesGameDto doublesGameDto = new DoublesGameDto();
            doublesGameDto.setId(id);
            BeanUtils.copyProperties(doublesGameOptional.get(), doublesGameDto);
            TeamWithPlayersDto team1 = new TeamWithPlayersDto();
            TeamWithPlayersDto team2 = new TeamWithPlayersDto();
            team1.setTeamWithStats(teamService.findTeam(doublesGameDto.getTeam1DatabaseId()));
            team2.setTeamWithStats(teamService.findTeam(doublesGameDto.getTeam2DatabaseId()));
            team1.setPlayers(team1.getTeamWithStats().getPlayerList().stream().map(uuid -> {
                try {
                    return userService.getUser(uuid);
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList()));
            team2.setPlayers(team2.getTeamWithStats().getPlayerList().stream().map(uuid -> {
                try {
                    return userService.getUser(uuid);
                } catch (UserNotFoundException e) {
                    e.printStackTrace();
                    return null;
                }
            }).collect(Collectors.toList()));
            doublesGameDto.setTeam1Details(team1);
            doublesGameDto.setTeam2Details(team2);
            return doublesGameDto;
        } else {
            throw new GameNotFoundException("This doubles game doesn't exist");
        }
    }

    @Override
    public List<DoublesGame> listGames() {
        return doublesRepository.findAll();
    }

    @Override
    public Page<DoublesGame> listGames(Pageable pageable) {
        return doublesRepository.findAll(pageable);
    }

    @Override
    public Page<DoublesGame> listAcceptedGames(Pageable pageable) {
        Page<DoublesGame> games = doublesRepository.findDoublesGameByAcceptedTrue(pageable);
        games = games.map(game -> {
            try {
                game.setTeam1Name(teamService.findTeam((game).getTeam1DatabaseId()).getName());
                game.setTeam2Name(teamService.findTeam((game).getTeam2DatabaseId()).getName());
                return game;
            } catch (CapserException e) {
                e.printStackTrace();
                return game;
            }
        });
        return games;
    }

    @Override
    protected void doProcessAchievements(User user, DoublesGame abstractGame) {
        achievementService.processDoublesAchievements(user, abstractGame);
    }

    @Override
    public Page<DoublesGame> listPlayerAcceptedGames(Pageable pageable, UUID team) {
        return doublesRepository.findDoublesGameByAcceptedTrueAndTeam1DatabaseIdEqualsOrTeam2DatabaseIdEquals(pageable, team, team);
    }

    @Override
    public GameType getGameType() {
        return GameType.DOUBLES;
    }
}
