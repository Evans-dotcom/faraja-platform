package com.example.farajaplatform.controller;

import com.example.farajaplatform.dto.AdminDto;
import com.example.farajaplatform.dto.AllPersons;
import com.example.farajaplatform.dto.SuccessandMessageDto;
import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.WidowProfile;
import com.example.farajaplatform.repository.AdminRepository;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.repository.WidowProfileRepository;
import com.example.farajaplatform.security.JWTGenerator;
import com.example.farajaplatform.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/")
public class AdminController {

    @Autowired
    AdminRepository adminRepository;
    @Autowired
    PersonRepository personRepository;
    Person person;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AdminService adminService;
    @Autowired
    JWTGenerator jwtGenerator;
    @Autowired
    MapperService mapperService;
    @Autowired
    FileUploaderService fileUploaderService;
    @Autowired
    ImageUploaderService imageUploaderService;
    @Autowired
    PersonService personService;
    @Autowired
    WidowService widowService;
    WidowProfile widowProfile;
    @Autowired
    WidowProfileRepository widowProfileRepository;

    @PostMapping("/api/v1/adminRegister")
    public ResponseEntity<String> adminRegister(@RequestBody AdminDto adminDto) {
        adminService.registerAdmin(adminDto.getUsername(), adminDto.getPassword());
        return new ResponseEntity<>("Admin Registered successfully!", HttpStatus.CREATED);
    }
//    @PostMapping("api/v1/adminRegister")
//    public ResponseEntity<String> adminRegister(@RequestBody AdminDto adminDto) {
//        if (adminRepository.existsByUsername(adminDto.getUsername())) {
//            return new ResponseEntity<String>("Username is taken !! ", HttpStatus.BAD_REQUEST);
//        }
//        Admin admin = new Admin();
//        admin.setUsername(adminDto.getUsername());
//        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
//
//        adminRepository.save(admin);
//        return new ResponseEntity<String>("Admin Registered successfully !! ", HttpStatus.CREATED);
//    }
//    @PostMapping("/api/v1/admin/personRegister")
//    public ResponseEntity<SuccessandMessageDto> registerPerson(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file,
//                                                               @RequestHeader(name = "Authorization") String token) throws IOException, UserAlreadyExistsException {
//
//        SuccessandMessageDto response = new SuccessandMessageDto();
//        try {
//            Person person = mapperService.mapForm(data, Person.class);
//            person.setFileName(fileUploaderService.uploadFile(file));
//            personService.registerPerson(person);
//            System.out.println(fileUploaderService.uploadFile(file));
//            response.setMessage("Member Registered Successfully !!");
//            response.setStatus(200);
//            String adminUsername = jwtGenerator.getUsernameFromJWT(token.substring(7));
//            person.setCreatedBy(adminRepository.findByUsername(jwtGenerator.getUsernameFromJWT(token.substring(7))).orElseThrow());
//            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);
//
//        } catch (UserAlreadyExistsException ex) {
//            response.setMessage("Email Already Taken");
//            response.setStatus(409);
//            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.CONFLICT);
//        }
//    }
    @PostMapping("/api/v1/admin/personRegister")
    public ResponseEntity<SuccessandMessageDto> registerPerson(@Valid @RequestPart("data") String data,
                                                               @RequestPart("file") MultipartFile file,
                                                               @RequestHeader(name = "Authorization") String token) throws UserAlreadyExistsException {
        SuccessandMessageDto response = new SuccessandMessageDto();
        adminService.registerPerson(data, file, token);
        response.setMessage("Member Registered Successfully !!");
        response.setStatus(200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/v1/admin/personProfile")
    public ResponseEntity<SuccessandMessageDto> registerWidowProfile(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file,
                                                                     @RequestHeader(name = "Authorization") String token) throws IOException, UserAlreadyExistsException, IOException {
        SuccessandMessageDto response = adminService.registerWidowProfile(data, file, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/api/v1/personDelete/{id}")
    public ResponseEntity<SuccessandMessageDto> deletePerson(@PathVariable("id") Integer id) {
        return adminService.deletePerson(id);
    }
    @PostMapping("/api/v1/admin/viewPersons")
    public AllPersons findAllPersons() {
        return adminService.findAllPersons();
    }
}
