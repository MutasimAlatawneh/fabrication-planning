package com.fabricationplanner.backend.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class SpoolMaterialResponse {
    private Long id;
    private Long materialId;
    private String materialCode;
    private String materialDescription;
    private BigDecimal qtyRequired;
    private BigDecimal availableQuantity;
}
