package com.fabricationplanner.backend.service;

import com.fabricationplanner.backend.dto.request.MaterialRequest;
import com.fabricationplanner.backend.dto.response.MaterialResponse;
import com.fabricationplanner.backend.exception.ResourceNotFoundException;
import com.fabricationplanner.backend.model.MaterialStock;
import com.fabricationplanner.backend.repository.MaterialStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialStockRepository materialStockRepository;

    public Page<MaterialResponse> getAll(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("code").ascending());
        Page<MaterialStock> materials;

        if (search != null && !search.isBlank()) {
            materials = materialStockRepository.search(search.trim(), pageRequest);
        } else {
            materials = materialStockRepository.findAll(pageRequest);
        }

        return materials.map(this::toResponse);
    }

    public MaterialResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public MaterialResponse create(MaterialRequest request) {
        MaterialStock material = MaterialStock.builder()
                .code(request.getCode().trim().toUpperCase())
                .description(request.getDescription().trim())
                .quantity(request.getQuantity())
                .build();
        return toResponse(materialStockRepository.save(material));
    }

    @Transactional
    public MaterialResponse update(Long id, MaterialRequest request) {
        MaterialStock material = findOrThrow(id);
        material.setCode(request.getCode().trim().toUpperCase());
        material.setDescription(request.getDescription().trim());
        material.setQuantity(request.getQuantity());
        return toResponse(materialStockRepository.save(material));
    }

    @Transactional
    public void delete(Long id) {
        MaterialStock material = findOrThrow(id);
        materialStockRepository.delete(material);
    }

    private MaterialStock findOrThrow(Long id) {
        return materialStockRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Material", id));
    }

    public MaterialResponse toResponse(MaterialStock m) {
        return MaterialResponse.builder()
                .id(m.getId())
                .code(m.getCode())
                .description(m.getDescription())
                .quantity(m.getQuantity())
                .build();
    }
}
