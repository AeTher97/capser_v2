package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.TeamWithStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository extends JpaRepository<TeamWithStats, UUID> {

    Optional<TeamWithStats> findTeamById(UUID id);

    Page<TeamWithStats> findByPlayerListContaining(Pageable pageable, UUID id);

    Page<TeamWithStats> findByNameContainingIgnoreCaseAndPlayerListNotContainingAndActive(Pageable pageable, String name, UUID id, boolean active);

    Page<TeamWithStats> findByNameContainingIgnoreCaseAndActive(Pageable pageable, String name, boolean active);


    Page<TeamWithStats> findByActiveTrueAndDoublesStatsGamesPlayedGreaterThanEqual(Pageable pageable, int games);

}
