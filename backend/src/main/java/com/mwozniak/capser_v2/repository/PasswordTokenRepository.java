package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordTokenRepository extends JpaRepository<PasswordResetToken, UUID> {

    Optional<PasswordResetToken> findPasswordResetTokenByToken(String token);

}
