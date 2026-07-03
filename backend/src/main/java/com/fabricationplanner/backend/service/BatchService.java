package com.fabricationplanner.backend.service;

import com.fabricationplanner.backend.dto.response.BatchGenerationResponse;
import com.fabricationplanner.backend.dto.response.BatchResponse;
import com.fabricationplanner.backend.dto.response.SpoolResponse;
import com.fabricationplanner.backend.model.*;
import com.fabricationplanner.backend.repository.*;
import com.fabricationplanner.backend.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchService {

    private final SpoolRepository spoolRepository;
    private final BatchRepository batchRepository;
    private final MaterialStockRepository materialStockRepository;
    private final SpoolService spoolService;

    @Transactional
    public BatchGenerationResponse generateBatch() {
        // 1. Fetch all spools that are waiting to be built
        List<Spool> pendingSpools = new ArrayList<>();
        pendingSpools.addAll(spoolRepository.findByStatus("PENDING"));
        pendingSpools.addAll(spoolRepository.findByStatus("PENDING_MATERIAL"));

        List<Spool> spoolsToBatch = new ArrayList<>();
        List<Spool> spoolsPending = new ArrayList<>();

        // 2. Evaluate material availability for each spool
        for (Spool spool : pendingSpools) {
            // Skip spools with no materials defined — cannot batch empty spools
            if (spool.getRequiredMaterials() == null || spool.getRequiredMaterials().isEmpty()) {
                spool.setStatus("PENDING_MATERIAL");
                spoolsPending.add(spool);
                spoolRepository.save(spool);
                continue;
            }

            boolean hasAllMaterials = true;

            for (SpoolMaterial requirement : spool.getRequiredMaterials()) {
                MaterialStock stock = requirement.getMaterialStock();

                // Check if warehouse stock is less than what this specific spool requires
                if (stock.getQuantity().compareTo(requirement.getQtyRequired()) < 0) {
                    hasAllMaterials = false;
                    break;
                }
            }

            if (hasAllMaterials) {
                // Deduct material quantities from the working state
                for (SpoolMaterial requirement : spool.getRequiredMaterials()) {
                    MaterialStock stock = requirement.getMaterialStock();
                    stock.setQuantity(stock.getQuantity().subtract(requirement.getQtyRequired()));
                    materialStockRepository.save(stock);
                }

                spool.setStatus("BATCHED");
                spoolsToBatch.add(spool);
            } else {
                spool.setStatus("PENDING_MATERIAL");
                spoolsPending.add(spool);
            }
            spoolRepository.save(spool);
        }

        // 3. Create and finalize the work order batch if items passed inspection
        String batchNo = null;
        if (!spoolsToBatch.isEmpty()) {
            Batch newBatch = Batch.builder()
                    .batchNo("BCH-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                    .createdDate(LocalDateTime.now())
                    .spools(spoolsToBatch)
                    .build();

            Batch saved = batchRepository.save(newBatch);
            batchNo = saved.getBatchNo();
        }

        // 4. Build response with both batched and pending lists
        List<SpoolResponse> batchedDtos = spoolsToBatch.stream()
                .map(spoolService::toResponse).collect(Collectors.toList());
        List<SpoolResponse> pendingDtos = spoolsPending.stream()
                .map(spoolService::toResponse).collect(Collectors.toList());

        String message = spoolsToBatch.isEmpty()
                ? "No spools met the material requirements for batching."
                : "Batch " + batchNo + " created with " + spoolsToBatch.size() + " spool(s).";

        return BatchGenerationResponse.builder()
                .message(message)
                .batchNo(batchNo)
                .batchedCount(spoolsToBatch.size())
                .pendingCount(spoolsPending.size())
                .batchedSpools(batchedDtos)
                .pendingSpools(pendingDtos)
                .build();
    }

    public Page<BatchResponse> getAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdDate").descending());
        return batchRepository.findAll(pageRequest).map(this::toResponse);
    }

    @Transactional(readOnly = true)
    public BatchResponse getById(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch", id));
        return toResponse(batch);
    }

    private BatchResponse toResponse(Batch b) {
        List<SpoolResponse> spoolDtos = b.getSpools() != null
                ? b.getSpools().stream().map(spoolService::toResponse).collect(Collectors.toList())
                : List.of();

        return BatchResponse.builder()
                .id(b.getId())
                .batchNo(b.getBatchNo())
                .createdDate(b.getCreatedDate())
                .spools(spoolDtos)
                .build();
    }
}