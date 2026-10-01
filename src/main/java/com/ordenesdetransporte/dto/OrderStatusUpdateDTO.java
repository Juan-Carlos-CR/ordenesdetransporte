package com.ordenesdetransporte.dto;

import com.ordenesdetransporte.domain.OrderStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderStatusUpdateDTO {
    @NotNull(message = "El estado es obligatorio")
    private OrderStatus status;
}
