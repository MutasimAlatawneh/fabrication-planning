package com.fabricationplanner.backend.service;

import com.fabricationplanner.backend.dto.response.DashboardResponse;
import com.fabricationplanner.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final IsoRepository isoRepository;
    private final SpoolRepository spoolRepository;
    private final MaterialStockRepository materialStockRepository;
    private final BatchRepository batchRepository;

    public DashboardResponse getMetrics() {
        return DashboardResponse.builder()
                .totalIsos(isoRepository.count())
                .totalSpools(spoolRepository.count())
                .availableMaterials(materialStockRepository.count())
                .generatedBatches(batchRepository.count())
                .pendingSpools(spoolRepository.countByStatus("PENDING_MATERIAL")
                             + spoolRepository.countByStatus("PENDING"))
                .batchedSpools(spoolRepository.countByStatus("BATCHED"))
                .build();
    }
}
