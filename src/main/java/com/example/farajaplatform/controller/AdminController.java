package com.example.farajaplatform.controller;

import com.example.farajaplatform.dto.AdminDto;
import com.example.farajaplatform.dto.AdminLoginResponseDto;
import com.example.farajaplatform.dto.AllPersons;
import com.example.farajaplatform.dto.SuccessandMessageDto;
import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.PersonProfile;
import com.example.farajaplatform.repository.AdminRepository;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.repository.PersonProfileRepository;
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
    PersonProfileService personProfileService;
    PersonProfile personProfile;
    @Autowired
    PersonProfileRepository personProfileRepository;

    @PostMapping("/api/v1/adminregister")
    public ResponseEntity<String> adminRegister(@RequestBody AdminDto adminDto) {
        adminService.registerAdmin(adminDto.getUsername(), adminDto.getPassword());
        return new ResponseEntity<>("Admin Registered successfully!", HttpStatus.CREATED);
    }
    @PostMapping("/api/v1/adminlogin")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminDto adminDto) {
        AdminLoginResponseDto response = adminService.login(adminDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @PostMapping("/api/v1/admin/personregister")
    public ResponseEntity<SuccessandMessageDto> registerPerson(@Valid @RequestPart("data") String data,
                                                               @RequestPart("file") MultipartFile file,
                                                               @RequestHeader(name = "Authorization") String token) throws UserAlreadyExistsException, IOException {
        SuccessandMessageDto response = new SuccessandMessageDto();
        adminService.registerPerson(data, file, token);
        response.setMessage("Member Registered Successfully !!");
        response.setStatus(200);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/api/v1/admin/createprofile")
    public ResponseEntity<SuccessandMessageDto> registerWidowProfile(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file,
                                                                     @RequestHeader(name = "Authorization") String token) throws UserAlreadyExistsException, IOException {
        SuccessandMessageDto response = adminService.registerWidowProfile(data, file, token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @DeleteMapping("/api/v1/persondelete/{id}")
    public ResponseEntity<SuccessandMessageDto> deletePerson(@PathVariable("id") Integer id) {
        return adminService.deletePerson(id);
    }
    @PostMapping("/api/v1/admin/viewpersons")
    public AllPersons findAllPersons() {
        return adminService.findAllPersons();
    }
}
