package com.ordenesdetransporte.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

@Data
public class DriverDTO {
    private UUID id;

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El número de licencia es obligatorio")
    private String licenseNumber;

    private boolean active = true;
}