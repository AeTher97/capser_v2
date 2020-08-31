package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.game.single.SinglesGame;
import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnrankedRepository extends JpaRepository<UnrankedGame, UUID> {

    Optional<UnrankedGame> findUnrankedGameById(UUID id);

    List<SinglesGame> findUnrankedGameByPlayer1OrPlayer2(UUID id, UUID id2);

}
