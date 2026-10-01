package com.ordenesdetransporte.dto;

import com.ordenesdetransporte.domain.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OrderResponseDTO {
    private UUID id;
    private OrderStatus status;
    private String origin;
    private String destination;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private DriverDTO driver;
    private String pdfAttachmentPath;
    private String imageAttachmentPath;
}
