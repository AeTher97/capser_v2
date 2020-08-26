package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.game.SinglesGame;
import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.SinglesRepository;
import com.mwozniak.capser_v2.repository.UsersRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final SinglesRepository singlesRepository;

    public UserService(UsersRepository usersRepository, SinglesRepository singlesRepository) {
        this.usersRepository = usersRepository;
        this.singlesRepository = singlesRepository;
    }

    public User getUser(UUID id) throws UserNotFoundException {
        Optional<User> userOptional = usersRepository.findUserById(id);
        if(userOptional.isPresent()){
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public void updatePlayerSinglesStats(User user, float pointsChange){
//        List<SinglesGame> singlesGameList = findUserSinglesGames(user.getId());
//
//        int gamesPlayed = singlesGameList.size();
//        int gamesWon = 0;
//        int gamesLost = 0;
//        int beersDowned = 0;
//
//        int totalPointsMade = 0;
//        int totalPointsLost = 0;
//
//        int totalSinksMade =0;
//        int totalSinksLost =0;
//        int nakedLaps =0;
//
//        int totalRebuttals = 0;
//
//        for(SinglesGame singlesGame : singlesGameList){
//            if(!singlesGame.isAccepted()){
//                continue;
//            }
//            gamesPlayed ++;
//            if(singlesGame.getWinner().equals(user.getId())){
//                gamesWon++;
//            } else {
//                gamesLost++;
//            }
//            GamePlayerStats stats;
//            GamePlayerStats opponentStats;
//            if(singlesGame.getPlayer1().equals(user.getId())){
//                stats = singlesGame.getPlayer1Stats();
//                opponentStats = singlesGame.getPlayer2Stats();
//            } else {
//                stats = singlesGame.getPlayer2Stats();
//                opponentStats = singlesGame.getPlayer1Stats();
//            }
//            beersDowned+=stats.getBeersDowned();
//            totalPointsLost += opponentStats.getScore();
//            totalPointsMade += stats.getScore();
//            totalSinksMade +=stats.getSinks();
//            totalSinksLost += opponentStats.getSinks();
//            if(stats.isNakedLap()){
//                nakedLaps++;
//            }
//
//        }
//
//        float avgRebuttals = (float)totalRebuttals / singlesGameList.size();
//        float winLossRatio = (float) gamesWon/gamesLost;
//        float sinksMadeLostRatio = (float) totalSinksMade/totalSinksLost;
//        float pointsMadeLostRatio = (float) totalPointsMade/totalPointsLost;
//
//        UserStats userStats = user.getUserSinglesStats();
//        userStats.setAvgRebuttals(avgRebuttals);
//        userStats.setBeersDowned(beersDowned);
//        userStats.setGamesLost(gamesLost);
//        userStats.setGamesWon(gamesWon);
//        userStats.setGamesPlayed(gamesPlayed);
//        userStats.setNakedLaps(nakedLaps);
//        userStats.setWinLossRatio(winLossRatio);
//        userStats.setSinksMadeLostRatio(sinksMadeLostRatio);
//        userStats.setPointsMadeLostRatio(pointsMadeLostRatio);
//        userStats.setTotalPointsLost(totalPointsLost);
//        userStats.setTotalPointsMade(totalPointsMade);
//        userStats.setTotalSinksMade(totalSinksMade);
//        userStats.setTotalSinksLost(totalSinksLost);
//
//        userStats.setPoints(userStats.getPoints()+pointsChange);
//
//        usersRepository.save(user);

    }

    private List<SinglesGame> findUserSinglesGames(UUID id){
//        return singlesRepository.findSinglesGamesByPlayer1OrPlayer2(id,id);
        return null;
    }
}
