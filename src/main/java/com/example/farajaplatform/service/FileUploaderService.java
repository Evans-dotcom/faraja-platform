package com.example.farajaplatform.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploaderService {

        public String uploadFile (MultipartFile file) throws IOException {
            if (file.isEmpty()){
                throw new IllegalArgumentException("File Is Empty");
            }
            try{
                String fileName = file.getOriginalFilename();
                String storageLocation = "C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\uploads\\Documents\\";
                String filePath = storageLocation + fileName;
                file.transferTo(new File(filePath));

                return filePath;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
}
