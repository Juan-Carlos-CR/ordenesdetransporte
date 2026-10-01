package com.ordenesdetransporte.service;

import com.ordenesdetransporte.exception.BadRequestException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path fileStorageLocation = Paths.get("uploads").toAbsolutePath().normalize();

    public FileStorageService() {
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("No se pudo crear el directorio para subir archivos.", ex);
        }
    }

    public String storeFile(MultipartFile file, String allowedExtension) {
        String fileName = file.getOriginalFilename();
        if (fileName == null || !fileName.toLowerCase().endsWith(allowedExtension)) {
            throw new BadRequestException("El archivo debe ser de formato " + allowedExtension);
        }

        try {
            String newFileName = UUID.randomUUID().toString() + "_" + fileName;
            Path targetLocation = this.fileStorageLocation.resolve(newFileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return targetLocation.toString();
        } catch (IOException ex) {
            throw new RuntimeException("Error al almacenar el archivo " + fileName, ex);
        }
    }
}
