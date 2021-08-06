package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SinglesRepository extends JpaRepository<SinglesGame, UUID> {

    Optional<SinglesGame> findSinglesGameById(UUID id);

    Page<SinglesGame> findSinglesGamesByAcceptedTrue(Pageable pageable);

    Page<SinglesGame> findSinglesGamesByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(Pageable pageable, UUID player1, UUID player2);

    List<SinglesGame> findSinglesGamesByPlayer1OrPlayer2(UUID id, UUID id2);

}
