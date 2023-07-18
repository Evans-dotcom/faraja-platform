package com.example.farajaplatform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.File;
import java.io.IOException;

@Service
public class ImageUploaderService {

    public String uploadImage(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is Empty");
        }
        try {
            String fileName = file.getOriginalFilename();
            String storageLocation = "C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\uploads\\";
            String filePath = storageLocation + fileName;
            file.transferTo(new File(filePath));

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}