package com.fabricationplanner.backend.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class BatchResponse {
    private Long id;
    private String batchNo;
    private LocalDateTime createdDate;
    private List<SpoolResponse> spools;
}
