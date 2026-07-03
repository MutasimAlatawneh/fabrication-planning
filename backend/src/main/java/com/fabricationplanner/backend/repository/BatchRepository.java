package com.fabricationplanner.backend.repository;

import com.fabricationplanner.backend.model.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch, Long> {
}