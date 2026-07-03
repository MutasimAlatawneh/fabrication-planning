package com.fabricationplanner.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "isos")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Iso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "iso_no", unique = true, nullable = false)
    private String isoNo;

    private String revision;
    private String status;

    @JsonIgnore
    @OneToMany(mappedBy = "iso", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Spool> spools = new ArrayList<>();
}