package com.fabricationplanner.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class SpoolRequest {

    @NotBlank(message = "Spool number is required")
    private String spoolNo;

    private String status = "PENDING";
}
