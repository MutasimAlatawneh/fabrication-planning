package com.fabricationplanner.backend.repository;

import com.fabricationplanner.backend.model.SpoolMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpoolMaterialRepository extends JpaRepository<SpoolMaterial, Long> {

    List<SpoolMaterial> findBySpoolId(Long spoolId);

    Optional<SpoolMaterial> findBySpoolIdAndMaterialStockId(Long spoolId, Long materialStockId);
}
