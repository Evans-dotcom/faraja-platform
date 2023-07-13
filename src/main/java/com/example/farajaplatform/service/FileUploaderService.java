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
            try{
                String fileName = file.getOriginalFilename();
                String storageLocation = "C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\src\\main\\java\\com\\example\\farajaplatform\\FilestorageLocation\\";
                String filePath = storageLocation + fileName;
                file.transferTo(new File(filePath));

                return filePath;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
}
