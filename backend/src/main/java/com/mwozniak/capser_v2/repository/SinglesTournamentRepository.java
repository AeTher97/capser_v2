package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.tournament.singles.SinglesTournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface SinglesTournamentRepository extends JpaRepository<SinglesTournament, UUID> {

    Optional<SinglesTournament> findSinglesTournamentById(UUID id);
}
