package com.fabricationplanner.backend.repository;

import com.fabricationplanner.backend.model.MaterialStock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MaterialStockRepository extends JpaRepository<MaterialStock, Long> {

    Optional<MaterialStock> findByCode(String code);

    @Query("SELECT m FROM MaterialStock m WHERE " +
           "LOWER(m.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(m.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<MaterialStock> search(@Param("search") String search, Pageable pageable);
}