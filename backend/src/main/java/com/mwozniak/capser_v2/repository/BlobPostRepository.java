package com.mwozniak.capser_v2.repository;

import com.mwozniak.capser_v2.models.database.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BlobPostRepository extends JpaRepository<Post, UUID> {
}
