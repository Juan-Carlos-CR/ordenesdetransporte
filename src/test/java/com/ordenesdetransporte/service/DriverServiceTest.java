package com.ordenesdetransporte.service;

import com.ordenesdetransporte.domain.Driver;
import com.ordenesdetransporte.exception.ResourceNotFoundException;
import com.ordenesdetransporte.repository.DriverRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverServiceTest {


    @Mock
    private DriverRepository driverRepository;

    @InjectMocks
    private DriverService driverService;

    private UUID driverId;
    private Driver mockDriver;

    @BeforeEach
    void setUp() {
        driverId = UUID.randomUUID();
        mockDriver = new Driver();
        mockDriver.setId(driverId);
        mockDriver.setName("Carlos Perez");
        mockDriver.setActive(true);
    }

    @Test
    @DisplayName("Debe retornar la entidad Driver cuando existe")
    void getDriverEntityById_Success() {
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(mockDriver));

        Driver result = driverService.getDriverEntityById(driverId);

        assertNotNull(result);
        assertEquals(driverId, result.getId());
        verify(driverRepository, times(1)).findById(driverId);
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException cuando el conductor no existe")
    void getDriverEntityById_NotFound_ThrowsException() {
        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> driverService.getDriverEntityById(driverId));
        verify(driverRepository, times(1)).findById(driverId);
    }
}