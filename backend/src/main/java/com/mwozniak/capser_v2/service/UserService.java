package com.mwozniak.capser_v2.service;

import com.mwozniak.capser_v2.models.database.User;
import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.exception.UserNotFoundException;
import com.mwozniak.capser_v2.repository.SinglesRepository;
import com.mwozniak.capser_v2.repository.UsersRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        if (userOptional.isPresent()) {
            return userOptional.get();
        } else {
            throw new UserNotFoundException("User not found");
        }
    }

    public  Optional<User> getUserOptional(UUID id)  {
        return usersRepository.findUserById(id);
    }


    public Optional<User> getUser(String username)  {
        return usersRepository.findUserByUsername(username);
    }

    public void saveUser(User user) {
        usersRepository.save(user);
    }

    public void updateLastSeen(User user) {
        user.setLastSeen(new Date());
        usersRepository.save(user);
    }

    public Page<User> getUsers(Pageable pageable) {
        return usersRepository.findAll(pageable);
    }

    private List<SinglesGame> findUserSinglesGames(UUID id) {
//        return singlesRepository.findSinglesGamesByPlayer1OrPlayer2(id,id);
        return null;
    }

}
