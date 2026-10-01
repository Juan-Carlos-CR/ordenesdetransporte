package com.ordenesdetransporte.service;


import com.ordenesdetransporte.dto.*;
import com.ordenesdetransporte.exception.BadRequestException;
import com.ordenesdetransporte.exception.ResourceNotFoundException;
import com.ordenesdetransporte.domain.*;
import com.ordenesdetransporte.repository.DriverRepository;
import com.ordenesdetransporte.repository.OrderRepository;
import com.ordenesdetransporte.repository.OrderSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverService {

    private final DriverRepository driverRepository;

    public DriverDTO createDriver(DriverDTO dto) {
        Driver driver = Driver.builder()
                .name(dto.getName())
                .licenseNumber(dto.getLicenseNumber())
                .active(dto.isActive())
                .build();
        return mapToDTO(driverRepository.save(driver));
    }

    public List<DriverDTO> getActiveDrivers() {
        return driverRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Driver getDriverEntityById(UUID id) {
        return driverRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Conductor no encontrado con id: " + id));
    }

    public DriverDTO mapToDTO(Driver driver) {
        if (driver == null) return null;
        DriverDTO dto = new DriverDTO();
        dto.setId(driver.getId());
        dto.setName(driver.getName());
        dto.setLicenseNumber(driver.getLicenseNumber());
        dto.setActive(driver.isActive());
        return dto;
    }
}
