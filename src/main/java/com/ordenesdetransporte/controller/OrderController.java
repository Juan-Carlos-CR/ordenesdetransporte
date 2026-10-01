package com.ordenesdetransporte.controller;

import com.ordenesdetransporte.domain.OrderStatus;
import com.ordenesdetransporte.dto.*;
import com.ordenesdetransporte.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        return new ResponseEntity<>(orderService.createOrder(dto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getOrders(
            @RequestParam(required = false) OrderStatus status,
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(orderService.getOrders(status, origin, destination, startDate, endDate));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable UUID id,
            @Valid @RequestBody OrderStatusUpdateDTO dto) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, dto.getStatus()));
    }

    @PostMapping(value = "/{id}/assign/{driverId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<OrderResponseDTO> assignDriver(
            @PathVariable UUID id,
            @PathVariable UUID driverId,
            @RequestPart(value = "pdf", required = false) MultipartFile pdf,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        return ResponseEntity.ok(orderService.assignDriver(id, driverId, pdf, image));
    }
}