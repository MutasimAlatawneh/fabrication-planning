package com.fabricationplanner.backend.service;

import com.fabricationplanner.backend.dto.request.SpoolMaterialRequest;
import com.fabricationplanner.backend.dto.request.SpoolRequest;
import com.fabricationplanner.backend.dto.response.SpoolMaterialResponse;
import com.fabricationplanner.backend.dto.response.SpoolResponse;
import com.fabricationplanner.backend.exception.ResourceNotFoundException;
import com.fabricationplanner.backend.model.*;
import com.fabricationplanner.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SpoolService {

    private final SpoolRepository spoolRepository;
    private final SpoolMaterialRepository spoolMaterialRepository;
    private final MaterialStockRepository materialStockRepository;
    private final IsoService isoService;

    public Page<SpoolResponse> getAll(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("spoolNo").ascending());
        Page<Spool> spools;

        if (search != null && !search.isBlank()) {
            spools = spoolRepository.search(search.trim(), pageRequest);
        } else {
            spools = spoolRepository.findAll(pageRequest);
        }

        return spools.map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public SpoolResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    public List<SpoolResponse> getByIsoId(Long isoId) {
        return spoolRepository.findByIsoId(isoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<SpoolResponse> getPendingSpools() {
        List<Spool> pending = spoolRepository.findByStatus("PENDING_MATERIAL");
        return pending.stream().map(this::toResponse).collect(Collectors.toList());
    }

    @Transactional
    public SpoolResponse create(Long isoId, SpoolRequest request) {
        Iso iso = isoService.findOrThrow(isoId);

        Spool spool = Spool.builder()
                .spoolNo(request.getSpoolNo().trim())
                .status(request.getStatus() != null ? request.getStatus() : "PENDING")
                .iso(iso)
                .build();

        return toResponse(spoolRepository.save(spool));
    }

    @Transactional
    public SpoolResponse update(Long id, SpoolRequest request) {
        Spool spool = findOrThrow(id);
        spool.setSpoolNo(request.getSpoolNo().trim());
        if (request.getStatus() != null) {
            spool.setStatus(request.getStatus());
        }
        return toResponse(spoolRepository.save(spool));
    }

    @Transactional
    public void delete(Long id) {
        Spool spool = findOrThrow(id);
        spoolRepository.delete(spool);
    }

    @Transactional
    public SpoolMaterialResponse addMaterial(Long spoolId, SpoolMaterialRequest request) {
        Spool spool = findOrThrow(spoolId);
        MaterialStock material = materialStockRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new ResourceNotFoundException("Material", request.getMaterialId()));

        // Check for duplicate mapping
        spoolMaterialRepository.findBySpoolIdAndMaterialStockId(spoolId, request.getMaterialId())
                .ifPresent(existing -> {
                    throw new IllegalArgumentException(
                            "Material '" + material.getCode() + "' is already assigned to this spool. " +
                            "Remove it first or update the existing requirement.");
                });

        SpoolMaterial sm = SpoolMaterial.builder()
                .spool(spool)
                .materialStock(material)
                .qtyRequired(request.getQtyRequired())
                .build();

        SpoolMaterial saved = spoolMaterialRepository.save(sm);
        return toSmResponse(saved);
    }

    @Transactional
    public void removeMaterial(Long spoolMaterialId) {
        SpoolMaterial sm = spoolMaterialRepository.findById(spoolMaterialId)
                .orElseThrow(() -> new ResourceNotFoundException("SpoolMaterial", spoolMaterialId));
        spoolMaterialRepository.delete(sm);
    }

    private Spool findOrThrow(Long id) {
        return spoolRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Spool", id));
    }

    public SpoolResponse toResponse(Spool s) {
        List<SpoolMaterialResponse> materials = s.getRequiredMaterials() != null
                ? s.getRequiredMaterials().stream().map(this::toSmResponse).collect(Collectors.toList())
                : Collections.emptyList();

        return SpoolResponse.builder()
                .id(s.getId())
                .spoolNo(s.getSpoolNo())
                .status(s.getStatus())
                .isoId(s.getIso().getId())
                .isoNo(s.getIso().getIsoNo())
                .requiredMaterials(materials)
                .build();
    }

    private SpoolMaterialResponse toSmResponse(SpoolMaterial sm) {
        return SpoolMaterialResponse.builder()
                .id(sm.getId())
                .materialId(sm.getMaterialStock().getId())
                .materialCode(sm.getMaterialStock().getCode())
                .materialDescription(sm.getMaterialStock().getDescription())
                .qtyRequired(sm.getQtyRequired())
                .availableQuantity(sm.getMaterialStock().getQuantity())
                .build();
    }
}
