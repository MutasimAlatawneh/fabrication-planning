package com.fabricationplanner.backend.repository;

import com.fabricationplanner.backend.model.Iso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IsoRepository extends JpaRepository<Iso, Long> {

    @Query("SELECT i FROM Iso i WHERE " +
           "LOWER(i.isoNo) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Iso> search(@Param("search") String search, Pageable pageable);
}