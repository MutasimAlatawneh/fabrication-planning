package com.fabricationplanner.backend.dto.response;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BatchGenerationResponse {
    private String message;
    private String batchNo;
    private int batchedCount;
    private int pendingCount;
    private List<SpoolResponse> batchedSpools;
    private List<SpoolResponse> pendingSpools;
}
