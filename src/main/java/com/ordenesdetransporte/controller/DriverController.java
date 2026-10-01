package com.ordenesdetransporte.controller;

import com.ordenesdetransporte.domain.Driver;
import com.ordenesdetransporte.repository.DriverRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverRepository driverRepository;

    @GetMapping
    public ResponseEntity<List<Driver>> getAllDrivers() {
        return ResponseEntity.ok(driverRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Driver> createDriver(@RequestBody Driver driver) {
        Driver savedDriver = driverRepository.save(driver);
        return new ResponseEntity<>(savedDriver, HttpStatus.CREATED);
    }
}