package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.game.team.DoublesGame;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoublesRepository extends JpaRepository<DoublesGame, UUID> {


    Optional<DoublesGame> findDoublesGameById(UUID id);

    Page<DoublesGame> findDoublesGameByAcceptedTrue(Pageable pageable);

    Page<DoublesGame> findDoublesGameByAcceptedTrueAndTeam1DatabaseIdEqualsOrTeam2DatabaseIdEquals(Pageable pageable, UUID team1, UUID team2);

}
