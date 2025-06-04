package com.zayyani.cafe.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;

@Service
public class FileStorageService {

    private final Path uploadDir = Paths.get("uploads");

    public FileStorageService() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Gagal membuat folder upload", e);
        }
    }

    public String storeFile(MultipartFile file) {
        try {
            String fileName = Path.of(file.getOriginalFilename()).getFileName().toString(); // sanitasi nama file
            Path destinationFile = this.uploadDir.resolve(fileName);
            file.transferTo(destinationFile.toFile());
            return fileName;
        } catch (IOException e) {
            throw new RuntimeException("Gagal menyimpan file", e);
        }
    }

    public Path loadFile(String fileName) {
        return uploadDir.resolve(fileName);
    }
}
