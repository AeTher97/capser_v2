package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.tournament.SinglesTournament;
import com.mwozniak.capser_v2.models.database.tournament.UnrankedTournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UnrankedTournamentRepository  extends JpaRepository<UnrankedTournament, UUID> {

    Optional<UnrankedTournament> findUnrankedTournamentById(UUID id);
}

