package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.UserStats;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StatsRepository extends JpaRepository<UserStats, UUID> {
}
