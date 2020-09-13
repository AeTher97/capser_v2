package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.AcceptanceRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AcceptanceRequestRepository  extends JpaRepository<AcceptanceRequest, UUID> {

    AcceptanceRequest findAcceptanceRequestById(UUID id);

    List<AcceptanceRequest> findAcceptanceRequestByGameToAccept(UUID id);

    List<AcceptanceRequest> findAcceptanceRequestByAcceptingUser(UUID id);
}
