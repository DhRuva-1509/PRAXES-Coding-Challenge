package com.telemedicine.backend.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultationRequest {

    @NotBlank(message = "Patient ID is required")
    private String parentId;

    @NotBlank(message = "Doctor ID is required")
    private String doctorId;
}
