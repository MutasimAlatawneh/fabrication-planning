package com.fabricationplanner.backend.dto.response;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpoolResponse {
    private Long id;
    private String spoolNo;
    private String status;
    private Long isoId;
    private String isoNo;
    private List<SpoolMaterialResponse> requiredMaterials;
}
