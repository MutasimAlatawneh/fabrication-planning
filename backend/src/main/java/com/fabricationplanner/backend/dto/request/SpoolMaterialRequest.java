package com.fabricationplanner.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SpoolMaterialRequest {

    @NotNull(message = "Material ID is required")
    private Long materialId;

    @NotNull(message = "Required quantity is required")
    @DecimalMin(value = "0.01", message = "Required quantity must be greater than 0")
    private BigDecimal qtyRequired;
}
