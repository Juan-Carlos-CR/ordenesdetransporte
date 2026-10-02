package com.ordenesdetransporte.service;

import com.ordenesdetransporte.dto.DriverDTO;
import com.ordenesdetransporte.domain.Driver;
import com.ordenesdetransporte.exception.ResourceNotFoundException;
import com.ordenesdetransporte.repository.DriverRepository;
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
        List<DriverDTO> activeDrivers = driverRepository.findByActiveTrue().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        if (activeDrivers.isEmpty()) {
            throw new ResourceNotFoundException("No se encontraron conductores activos registrados");
        }

        return activeDrivers;
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