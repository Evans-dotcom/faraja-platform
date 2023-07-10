package com.example.farajaplatform.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ImageUploaderService {

    public String uploadImage(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        String storageLocation = "C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\src\\main\\resources\\Profiles\\";
        String filePath = storageLocation + fileName;
        file.transferTo(new File(filePath));

        return filePath;

    }
}
//public String uploadImage(MultipartFile file) {
//    try{
//        file.transferTo(new File("C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\src\\main\\resources\\Profiles\\"+file.getOriginalFilename()));
//    }catch(Exception e){
//        System.out.println(e);
//    }
//    return file.getOriginalFilename();
//}
//}
