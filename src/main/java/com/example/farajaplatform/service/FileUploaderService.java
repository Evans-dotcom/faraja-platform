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



//    @Autowired
//    ResourceLoader resourceLoader;
//    public String uploadFile(MultipartFile file) {
//        try {
//            String fileName = file.getOriginalFilename();
//            String storageLocation = "classpath:Documents";
//            Resource directory = resourceLoader.getResource(storageLocation);
//            String filePath = directory.getFile().getPath();
////
////            File destinationFile = new File(filePath);
////            file.transferTo(destinationFile);
//            System.out.println(filePath);
//            return filePath;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//}
//    public String uploadFile(MultipartFile file) {
//        try{
//            file.transferTo(new File("C:\\Users\\emmanuel kimutai\\Downloads\\faraja-platform\\src\\main\\resources\\Documents\\"+file.getOriginalFilename()));
//        }catch(Exception e){
//            System.out.println(e);
//        }
//        return file.getOriginalFilename();
//    }
//}
