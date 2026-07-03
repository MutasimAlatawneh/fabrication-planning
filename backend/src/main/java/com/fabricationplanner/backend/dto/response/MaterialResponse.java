package com.fabricationplanner.backend.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class MaterialResponse {
    private Long id;
    private String code;
    private String description;
    private BigDecimal quantity;
}
