package com.fabricationplanner.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "spools")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Spool {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spool_no", unique = true, nullable = false)
    private String spoolNo;

    @Column(nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "iso_id", nullable = false)
    private Iso iso;

    @OneToMany(mappedBy = "spool", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<SpoolMaterial> requiredMaterials = new ArrayList<>();
}