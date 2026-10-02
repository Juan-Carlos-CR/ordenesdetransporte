package com.ordenesdetransporte.controller;

import com.ordenesdetransporte.dto.DriverDTO;
import com.ordenesdetransporte.service.DriverService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    @GetMapping("/active")
    public ResponseEntity<List<DriverDTO>> getActiveDrivers() {
        return ResponseEntity.ok(driverService.getActiveDrivers());
    }

    @PostMapping
    public ResponseEntity<DriverDTO> createDriver(@RequestBody DriverDTO dto) {
        return new ResponseEntity<>(driverService.createDriver(dto), HttpStatus.CREATED);
    }
}