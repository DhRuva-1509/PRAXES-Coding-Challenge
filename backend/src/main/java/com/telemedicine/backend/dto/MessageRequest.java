package com.telemedicine.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageRequest {

    @NotBlank(message = "Author is required")
    private String author;

    @NotBlank(message = "Author role is required")
    @Pattern(regexp = "PATIENT|DOCTOR", message = "Author role must be either PATIENT or DOCTOR")
    private String authorRole;

    @NotBlank(message = "Content is required")
    private String content;
}
