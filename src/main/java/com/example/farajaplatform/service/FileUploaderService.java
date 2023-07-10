package com.example.farajaplatform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class FileUploaderService {

    public String uploadFile(MultipartFile file) {
        try {
            String fileName = file.getOriginalFilename();
            String storageLocation = "C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\src\\main\\resources\\Documents\\";
            String filePath = storageLocation + fileName;

            File destinationFile = new File(filePath);
            file.transferTo(destinationFile);

            return filePath;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}

//    public String uploadFile(MultipartFile file) {
//        try{
//            file.transferTo(new File("C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\src\\main\\resources\\Documents\\"+file.getOriginalFilename()));
//        }catch(Exception e){
//            System.out.println(e);
//        }
//        return file.getOriginalFilename();
//    }
//}
