package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.game.single.SoloGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SinglesRepository extends JpaRepository<SoloGame, UUID> {

    Optional<SoloGame> findSinglesGameById(UUID id);

    Page<SoloGame> findSinglesGamesByAcceptedTrue(Pageable pageable);

    Page<SoloGame> findSinglesGamesByAcceptedTrueAndPlayer1EqualsOrPlayer2Equals(Pageable pageable, UUID player1, UUID player2);

    @Query(value = "select * from singles_game u where ((CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player1 as text)=CAST(?2 as text)) AND (CAST(u.player2 as text) =CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?2 as text))) OR ((?2 IS NULL ) AND (CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?1 as text)))",
            nativeQuery = true)
    Page<SoloGame> findSinglesGamesWithPlayerAndOpponent(Pageable pageable, UUID player1, UUID player2);

    @Query(value = "select * from singles_game u where ((CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player1 as text)=CAST(?2 as text)) AND (CAST(u.player2 as text) =CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?2 as text))) OR ((?2 IS NULL ) AND (CAST(u.player1 as text) = CAST(?1 as text) OR CAST(u.player2 as text)=CAST(?1 as text)))",
            nativeQuery = true)
    List<SoloGame> findSinglesGamesWithPlayerAndOpponent(UUID player1, UUID player2);

    List<SoloGame> findSinglesGamesByPlayer1OrPlayer2(UUID id, UUID id2);

}
