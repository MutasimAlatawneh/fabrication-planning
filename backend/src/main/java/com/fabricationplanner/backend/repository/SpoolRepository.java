package com.fabricationplanner.backend.repository;

import com.fabricationplanner.backend.model.Spool;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SpoolRepository extends JpaRepository<Spool, Long> {

    List<Spool> findByStatus(String status);

    List<Spool> findByIsoId(Long isoId);

    long countByStatus(String status);

    @Query("SELECT s FROM Spool s JOIN s.iso i WHERE " +
           "LOWER(s.spoolNo) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(i.isoNo) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Spool> search(@Param("search") String search, Pageable pageable);
}