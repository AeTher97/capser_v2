package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.game.AbstractGame;
import com.mwozniak.capser_v2.models.database.game.SinglesGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SinglesRepository extends JpaRepository<SinglesGame, UUID> {

    Optional<SinglesGame> findSinglesGameById(UUID id);

    List<SinglesGame> findSinglesGamesByPlayer1OrPlayer2(UUID id, UUID id2);
}
