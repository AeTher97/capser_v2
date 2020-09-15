package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.game.single.EasyCapsGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EasyCapsRepository extends JpaRepository<EasyCapsGame, UUID> {

    Optional<EasyCapsGame> findEasyCapsGameById(UUID id);

    Page<EasyCapsGame> findEasyCapsGamesByAcceptedTrue(Pageable pageable);

}
