package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.SinglesGame;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SinglesRepository  extends JpaRepository<SinglesGame, UUID> {
}
