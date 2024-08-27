package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.game.single.UnrankedGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UnrankedRepository extends JpaRepository<UnrankedGame, UUID> {

    Optional<UnrankedGame> findUnrankedGameById(UUID id);

    List<UnrankedGame> findUnrankedGameByPlayer1OrPlayer2(UUID id, UUID id2);

    Page<UnrankedGame> findUnrankedGameByAcceptedTrue(Pageable pageable);

    Page<UnrankedGame> findUnrankedGameByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(Pageable pageable, UUID player1, UUID player2);

    @Query(value = "select * from unranked_game u where ((CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player1 as text)=CAST(?2 as text)) AND (CAST(u.player2 as text) =CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?2 as text))) OR ((?2 IS NULL ) AND (CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?1 as text)))",
            nativeQuery = true)
    Page<UnrankedGame> findUnrankedGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2);

    @Query(value = "select * from unranked_game u where ((CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player1 as text)=CAST(?2 as text)) AND (CAST(u.player2 as text) =CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?2 as text))) OR ((?2 IS NULL ) AND (CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?1 as text)))",
            nativeQuery = true)
    List<UnrankedGame> findUnrankedGamesWithPlayerAndOpponent(UUID player1, UUID player2);
}
