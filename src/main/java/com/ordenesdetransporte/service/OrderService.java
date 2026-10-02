package com.ordenesdetransporte.service;


import com.ordenesdetransporte.dto.*;
import com.ordenesdetransporte.exception.BadRequestException;
import com.ordenesdetransporte.exception.ResourceNotFoundException;
import com.ordenesdetransporte.domain.*;
import com.ordenesdetransporte.repository.OrderRepository;
import com.ordenesdetransporte.repository.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final DriverService driverService;
    private final FileStorageService fileStorageService;

    public OrderResponseDTO createOrder(OrderCreateDTO dto) {
        Order order = Order.builder()
                .origin(dto.getOrigin())
                .destination(dto.getDestination())
                .status(OrderStatus.CREATED)
                .build();
        return mapToDTO(orderRepository.save(order));
    }

    public OrderResponseDTO getOrderById(UUID id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));
        return mapToDTO(order);
    }

    public List<OrderResponseDTO> getOrders(OrderStatus status, String origin, String destination, LocalDateTime startDate, LocalDateTime endDate) {
        Specification<Order> spec = OrderSpecification.filterBy(status, origin, destination, startDate, endDate);

        List<Order> orders = orderRepository.findAll(spec);

        if (orders.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron órdenes con los filtros especificados");
        }

        return orders.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrderResponseDTO updateOrderStatus(UUID id, OrderStatus newStatus) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada con id: " + id));

        validateStatusTransition(order.getStatus(), newStatus);
        order.setStatus(newStatus);
        return mapToDTO(orderRepository.save(order));
    }

    @Transactional
    public OrderResponseDTO assignDriver(UUID orderId, UUID driverId, MultipartFile pdf, MultipartFile image) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Orden no encontrada"));

        if (order.getStatus() != OrderStatus.CREATED) {
            throw new BadRequestException("Solo se puede asignar un conductor a una orden en estado CREATED");
        }

        Driver driver = driverService.getDriverEntityById(driverId);
        if (!driver.isActive()) {
            throw new BadRequestException("El conductor debe estar activo");
        }

        if (pdf != null && !pdf.isEmpty()) {
            order.setPdfAttachmentPath(fileStorageService.storeFile(pdf, ".pdf"));
        }
        if (image != null && !image.isEmpty()) {
            String fileName = image.getOriginalFilename() != null ? image.getOriginalFilename().toLowerCase() : "";
            if (!fileName.endsWith(".png") && !fileName.endsWith(".jpg") && !fileName.endsWith(".jpeg")) {
                throw new BadRequestException("La imagen debe ser de formato .png o .jpg");
            }
            order.setImageAttachmentPath(fileStorageService.storeFile(image, fileName.substring(fileName.lastIndexOf("."))));
        }

        order.setDriver(driver);
        return mapToDTO(orderRepository.save(order));
    }

    private void validateStatusTransition(OrderStatus current, OrderStatus next) {
        if (current == next) return;
        boolean valid = switch (current) {
            case CREATED -> next == OrderStatus.IN_TRANSIT || next == OrderStatus.CANCELLED;
            case IN_TRANSIT -> next == OrderStatus.DELIVERED || next == OrderStatus.CANCELLED;
            case DELIVERED, CANCELLED -> false;
        };

        if (!valid) {
            throw new BadRequestException("Transición de estado no válida de " + current + " a " + next);
        }
    }

    private OrderResponseDTO mapToDTO(Order order) {
        OrderResponseDTO dto = new OrderResponseDTO();
        dto.setId(order.getId());
        dto.setStatus(order.getStatus());
        dto.setOrigin(order.getOrigin());
        dto.setDestination(order.getDestination());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setUpdatedAt(order.getUpdatedAt());
        dto.setPdfAttachmentPath(order.getPdfAttachmentPath());
        dto.setImageAttachmentPath(order.getImageAttachmentPath());
        dto.setDriver(driverService.mapToDTO(order.getDriver()));
        return dto;
    }
}
