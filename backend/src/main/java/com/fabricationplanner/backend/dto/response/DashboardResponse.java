package com.fabricationplanner.backend.dto.response;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardResponse {
    private long totalIsos;
    private long totalSpools;
    private long availableMaterials;
    private long generatedBatches;
    private long pendingSpools;
    private long batchedSpools;
}
