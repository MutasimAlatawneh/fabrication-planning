package com.fabricationplanner.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Entity
@Table(name = "spool_materials")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpoolMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "spool_id", nullable = false)
    private Spool spool;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    private MaterialStock materialStock;

    // This is the field the BatchService was looking for!
    @Column(name = "qty_required", nullable = false, precision = 10, scale = 2)
    private BigDecimal qtyRequired;
}