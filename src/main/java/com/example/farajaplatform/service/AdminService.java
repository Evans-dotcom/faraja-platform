package com.example.farajaplatform.service;

import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Admin;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.repository.AdminRepository;
import com.example.farajaplatform.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    JWTGenerator jwtGenerator;
    @Autowired
    MapperService mapperService;
    @Autowired
    FileUploaderService fileUploaderService;
    @Autowired
    PersonService personService;

    public boolean isAdminUsernameTaken(String username) {
        return adminRepository.existsByUsername(username);
    }

    public void registerAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        adminRepository.save(admin);
    }
    public void registerPerson(String data, MultipartFile file, String token) throws UserAlreadyExistsException {
        try {
            Person person = mapperService.mapForm(data, Person.class);
            person.setFileName(fileUploaderService.uploadFile(file));
            personService.registerPerson(person);

            String adminUsername = jwtGenerator.getUsernameFromJWT(token.substring(7));
            person.setCreatedBy(adminRepository.findByUsername(adminUsername).orElseThrow());
        } catch (UserAlreadyExistsException ex) {
            throw new UserAlreadyExistsException();
        } catch (IOException ex) {
            throw new RuntimeException("Error processing the request.", ex);
        }
    }



}
