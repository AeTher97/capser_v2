package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.tournament.doubles.DoublesTournament;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DoublesTournamentRepository  extends JpaRepository<DoublesTournament,UUID> {
    Optional<DoublesTournament> findDoublesTournamentById(UUID id);

}
