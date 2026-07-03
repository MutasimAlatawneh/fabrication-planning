package com.fabricationplanner.backend.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class MaterialRequest {

    @NotBlank(message = "Material code is required")
    private String code;

    @NotBlank(message = "Description is required")
    private String description;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.00", message = "Quantity cannot be negative")
    private BigDecimal quantity;
}
