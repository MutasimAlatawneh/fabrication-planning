package com.fabricationplanner.backend.dto.response;

import lombok.*;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class IsoResponse {
    private Long id;
    private String isoNo;
    private String revision;
    private String status;
    private List<SpoolResponse> spools;
}
