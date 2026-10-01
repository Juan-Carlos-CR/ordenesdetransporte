package com.ordenesdetransporte.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrderCreateDTO {
    @NotBlank(message = "El origen es obligatorio")
    private String origin;

    @NotBlank(message = "El destino es obligatorio")
    private String destination;
}
