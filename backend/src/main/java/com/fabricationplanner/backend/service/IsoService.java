package com.fabricationplanner.backend.service;

import com.fabricationplanner.backend.dto.request.IsoRequest;
import com.fabricationplanner.backend.dto.response.IsoResponse;
import com.fabricationplanner.backend.dto.response.SpoolMaterialResponse;
import com.fabricationplanner.backend.dto.response.SpoolResponse;
import com.fabricationplanner.backend.exception.ResourceNotFoundException;
import com.fabricationplanner.backend.model.Iso;
import com.fabricationplanner.backend.model.Spool;
import com.fabricationplanner.backend.model.SpoolMaterial;
import com.fabricationplanner.backend.repository.IsoRepository;
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
public class IsoService {

    private final IsoRepository isoRepository;

    public Page<IsoResponse> getAll(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("isoNo").ascending());
        Page<Iso> isos;

        if (search != null && !search.isBlank()) {
            isos = isoRepository.search(search.trim(), pageRequest);
        } else {
            isos = isoRepository.findAll(pageRequest);
        }

        return isos.map(this::toResponseBrief);
    }

    @Transactional(readOnly = true)
    public IsoResponse getById(Long id) {
        Iso iso = findOrThrow(id);
        return toResponseFull(iso);
    }

    @Transactional
    public IsoResponse create(IsoRequest request) {
        Iso iso = Iso.builder()
                .isoNo(request.getIsoNo().trim())
                .revision(request.getRevision().trim())
                .status(request.getStatus() != null ? request.getStatus() : "RELEASED")
                .build();
        return toResponseBrief(isoRepository.save(iso));
    }

    @Transactional
    public IsoResponse update(Long id, IsoRequest request) {
        Iso iso = findOrThrow(id);
        iso.setIsoNo(request.getIsoNo().trim());
        iso.setRevision(request.getRevision().trim());
        if (request.getStatus() != null) {
            iso.setStatus(request.getStatus());
        }
        return toResponseBrief(isoRepository.save(iso));
    }

    @Transactional
    public void delete(Long id) {
        Iso iso = findOrThrow(id);
        isoRepository.delete(iso);
    }

    public Iso findOrThrow(Long id) {
        return isoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ISO", id));
    }

    /** Brief response without loading spools (for list views) */
    private IsoResponse toResponseBrief(Iso iso) {
        return IsoResponse.builder()
                .id(iso.getId())
                .isoNo(iso.getIsoNo())
                .revision(iso.getRevision())
                .status(iso.getStatus())
                .spools(null)
                .build();
    }

    /** Full response with spools loaded (for detail view) */
    private IsoResponse toResponseFull(Iso iso) {
        List<SpoolResponse> spoolDtos = iso.getSpools() != null
                ? iso.getSpools().stream().map(this::toSpoolResponse).collect(Collectors.toList())
                : Collections.emptyList();

        return IsoResponse.builder()
                .id(iso.getId())
                .isoNo(iso.getIsoNo())
                .revision(iso.getRevision())
                .status(iso.getStatus())
                .spools(spoolDtos)
                .build();
    }

    private SpoolResponse toSpoolResponse(Spool s) {
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
