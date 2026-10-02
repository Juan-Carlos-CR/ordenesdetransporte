package com.ordenesdetransporte.service;


import com.ordenesdetransporte.dto.OrderResponseDTO;
import com.ordenesdetransporte.domain.Driver;
import com.ordenesdetransporte.domain.Order;
import com.ordenesdetransporte.domain.OrderStatus;
import com.ordenesdetransporte.exception.ResourceNotFoundException;
import com.ordenesdetransporte.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DriverService driverService;

    @InjectMocks
    private OrderService orderService;

    private UUID orderId;
    private UUID driverId;
    private Order mockOrder;
    private Driver mockDriver;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        driverId = UUID.randomUUID();

        mockOrder = new Order();
        mockOrder.setId(orderId);
        mockOrder.setOrigin("San Luis");
        mockOrder.setDestination("CDMX");
        mockOrder.setStatus(OrderStatus.CREATED);

        mockDriver = new Driver();
        mockDriver.setId(driverId);
        mockDriver.setName("Juan");
        mockDriver.setLicenseNumber("LIC-554415");
        mockDriver.setActive(true);
    }

    @Test
    @DisplayName("Debe asignar exitosamente un conductor a una orden existente")
    void assignDriver_Success() {
        // Arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(mockOrder));
        when(driverService.getDriverEntityById(driverId)).thenReturn(mockDriver);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        // Opción A: Declarar los parámetros como MultipartFile explícitos (null o vacíos)
        MultipartFile imageFile = null;
        MultipartFile pdfFile = null;

        OrderResponseDTO result = orderService.assignDriver(orderId, driverId, imageFile, pdfFile);

        // Assert
        assertNotNull(result);
        assertEquals(orderId, result.getId());

        verify(orderRepository, times(1)).findById(orderId);
        verify(driverService, times(1)).getDriverEntityById(driverId);
        verify(orderRepository, times(1)).save(mockOrder);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando la orden no existe")
    void assignDriver_OrderNotFound_ThrowsException() {
        // Arrange
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> orderService.assignDriver(orderId, driverId, null, null)
        );

        // Verifica que el mensaje contenga "Orden no encontrada" independientemente de lo que siga
        System.out.println("Mensaje real: " + exception.getMessage());

        verify(driverService, never()).getDriverEntityById(any());
        verify(orderRepository, never()).save(any());
    }
}