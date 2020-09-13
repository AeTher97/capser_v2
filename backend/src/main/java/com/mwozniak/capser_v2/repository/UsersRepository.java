package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<User, UUID> {

    Optional<User> findUserById(UUID id);
    Optional<User> findUserByUsername(String username);

    Page<User> findByUsernameContainingIgnoreCase(String username, Pageable pageable);


}
