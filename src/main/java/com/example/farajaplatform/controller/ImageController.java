package com.example.farajaplatform.controller;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("/")
public class ImageController {

    private final String storageLocation = "C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\uploads\\";

    @PostMapping("images/{filename}")
    public ResponseEntity<byte[]> getImage(@PathVariable("filename") String filename) {
        String filePath = storageLocation + filename;
        File file = new File(filePath);

        if (file.exists()) {
            try {
                byte[] imageBytes = Files.readAllBytes(file.toPath());
                return ResponseEntity.ok()
                        .contentType(MediaType.IMAGE_JPEG)
                        .body(imageBytes);
            } catch (IOException e) {
                //handles exception that may occur while reading the File
                e.printStackTrace();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
