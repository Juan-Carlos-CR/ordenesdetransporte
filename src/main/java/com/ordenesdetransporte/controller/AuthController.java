package com.ordenesdetransporte.controller;

import com.ordenesdetransporte.dto.AuthRequestDTO;
import com.ordenesdetransporte.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody AuthRequestDTO request) {
        if ("admin".equals(request.getUsername()) && "admin123".equals(request.getPassword())) {
            String token = jwtService.generateToken(request.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }
        return ResponseEntity.status(401).body(Map.of("error", "Credenciales inválidas"));
    }
}