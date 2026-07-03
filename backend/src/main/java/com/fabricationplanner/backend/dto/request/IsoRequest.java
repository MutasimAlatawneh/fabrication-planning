package com.fabricationplanner.backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class IsoRequest {

    @NotBlank(message = "ISO number is required")
    private String isoNo;

    @NotBlank(message = "Revision is required")
    private String revision;

    private String status = "RELEASED";
}
