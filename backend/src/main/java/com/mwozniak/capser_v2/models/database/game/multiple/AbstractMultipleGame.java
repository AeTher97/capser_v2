package com.mwozniak.capser_v2.models.database.game.multiple;

import com.mwozniak.capser_v2.enums.GameMode;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.GamePlayerStats;
import com.mwozniak.capser_v2.models.dto.AbstractGameDto;
import com.mwozniak.capser_v2.models.dto.MultipleGameDto;
import com.mwozniak.capser_v2.models.exception.GameValidationException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@MappedSuperclass
public abstract class AbstractMultipleGame extends AbstractGame {

    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "team_1", referencedColumnName = "id", nullable = false)
    private Team team1;

    @Setter
    @Getter
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "team_2", referencedColumnName = "id", nullable = false)
    private Team team2;

    @Setter
    @Getter
    private UUID team1DatabaseId;

    @Setter
    @Getter
    private UUID team2DatabaseId;

    @Setter
    @Getter
    private int team1Score;

    @Setter
    @Getter
    private int team2Score;

    @Setter
    private UUID winner;

    public Team getWinner() {

        if (team1.getId().equals(winner)) {
            return team1;
        } else {
            return team2;
        }
    }

    public Team getLoser() {

        if (team1.getId().equals(winner)) {
            return team2;
        } else {
            return team1;
        }
    }


    @Override
    public void validateGame() throws GameValidationException {


        if (getTeam1Score() == getTeam2Score()) {
            throw new GameValidationException("Game cannot end in a draw");
        }

        if (getTeam1Score() < 11 && getTeam2Score() < 11) {
            throw new GameValidationException("Game must end with one of the teams obtaining 11 points");
        }

        if (getGameMode().equals(GameMode.OVERTIME) && Math.abs(getTeam1Score() - getTeam2Score()) != 2) {
            throw new GameValidationException("Overtime game must finish with 2 points advantage");
        } else if (getGameMode().equals(GameMode.SUDDEN_DEATH) && (getTeam1Score() != 11 && getTeam2Score() != 11)) {
            throw new GameValidationException("Sudden death game must finish with 11 points");
        }


        if (getTeamPoints(getTeam1Stats()) != getTeam1Score() && getTeamPoints(getTeam1Stats()) != 0) {
            throw new GameValidationException("All players score in team 1 has to sum to team score");
        }

        if (getTeamPoints(getTeam2Stats()) != getTeam2Score() && getTeamPoints(getTeam2Stats()) != 0) {
            throw new GameValidationException("All players score in team 2 has to sum to team score");
        }

        continueValidation();

    }

    @Override
    public final void calculateGameStats() throws GameValidationException {
        if (team1Score > team2Score) {
            setWinner(team1DatabaseId);
        } else {
            setWinner(team2DatabaseId);
        }

        gamePlayerStats.forEach(stats -> {
            if (stats.getScore() == 0) {
                stats.setNakedLap(true);
                setNakedLap(true);
            }
        });

        calculateBeers(getTeam1Stats(), getTeam2Stats());

    }

    @Override
    public void fillCommonProperties(AbstractGameDto abstractGameDto) {
        MultipleGameDto multipleGameDtoCast = (MultipleGameDto) abstractGameDto;
        setGameEventList(abstractGameDto.getGameEventList());
        setGameMode(abstractGameDto.getGameMode());

        team1 = new Team();
        team2 = new Team();

        team1DatabaseId = multipleGameDtoCast.getTeam1();
        team2DatabaseId = multipleGameDtoCast.getTeam2();

        team1.setPlayerList(multipleGameDtoCast.getTeam1Players());
        team2.setPlayerList(multipleGameDtoCast.getTeam2Players());
        setTeam1(team1);
        setTeam2(team2);

        team1Score = multipleGameDtoCast.getTeam1Score();
        team2Score = multipleGameDtoCast.getTeam2Score();

        setGamePlayerStats(multipleGameDtoCast.getPlayerStatsDtos().stream()
                .map(dto -> GamePlayerStats.builder()
                        .score(dto.getScore())
                        .sinks(dto.getSinks())
                        .rebuttals(dto.getRebuttals())
                        .playerId(dto.getPlayerId())
                        .build()).collect(Collectors.toList()));

    }


    protected void calculateBeers(List<GamePlayerStats> team1, List<GamePlayerStats> team2) {
        float team1BeersToSplit = 0;
        if (team2Score > 8) {
            team1BeersToSplit = 3;
        } else if (team2Score > 4) {
            team1BeersToSplit = 2;
        } else {
            team1BeersToSplit = 1;
        }

        float team2BeersToSplit = 0;
        if (team1Score > 8) {
            team2BeersToSplit = 3;
        } else if (team1Score > 4) {
            team2BeersToSplit = 2;
        } else {
            team2BeersToSplit = 1;
        }
        float finalTeam1BeersToSplit = team1BeersToSplit;
        team1.forEach(stats -> stats.setBeersDowned(finalTeam1BeersToSplit / team1.size()));
        float finalTeam2BeersToSplit = team2BeersToSplit;
        team2.forEach(stats -> stats.setBeersDowned(finalTeam2BeersToSplit / team2.size()));
    }


    protected List<GamePlayerStats> getTeam1Stats() {
        return gamePlayerStats.stream().filter(stats -> team1.getPlayerList().contains(stats.getPlayerId())).collect(Collectors.toList());
    }

    protected List<GamePlayerStats> getTeam2Stats() {
        return gamePlayerStats.stream().filter(stats -> team2.getPlayerList().contains(stats.getPlayerId())).collect(Collectors.toList());
    }

    @Override
    public int getSinksLost(User user) {
        if (getTeam1().getPlayerList().contains(user.getId())) {
            return getTeamSinks(getTeam2Stats());
        } else {
            return getTeamSinks(getTeam1Stats());
        }
    }

    @Override
    public int getPointsLost(User user) {
        if (getTeam1().getPlayerList().contains(user.getId())) {
            return getTeamPoints(getTeam2Stats());
        } else {
            return getTeamPoints(getTeam1Stats());
        }
    }

    public UUID getWinnerTeamId(){
        return winner;
    }

    public UUID getLoserTeamId(){
        if(winner.equals(team1DatabaseId)){
            return team2DatabaseId;
        } else {
            return team1DatabaseId;
        }
    }

    private int getTeamPoints(List<GamePlayerStats> stats) {
        return stats.stream().map(GamePlayerStats::getScore).mapToInt(Integer::intValue).sum();
    }

    private int getTeamSinks(List<GamePlayerStats> stats) {
        return stats.stream().map(GamePlayerStats::getSinks).mapToInt(Integer::intValue).sum();
    }

    protected boolean isWinner(User user) {
        return getWinner().getPlayerList().contains(user.getId());
    }

    protected abstract void continueValidation() throws GameValidationException;

}
