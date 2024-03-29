package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.tournament.singles.EasyCapsTournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface EasyCapsTournamentRepository extends JpaRepository<EasyCapsTournament, UUID> {

    Optional<EasyCapsTournament> findEasyCapsTournamentById(UUID id);
}
