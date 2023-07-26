package com.example.farajaplatform.controller;

import com.example.farajaplatform.dto.*;
import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.exception.ProfileAlreadyExistsException;
import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.UserType;
import com.example.farajaplatform.model.PersonProfile;
import com.example.farajaplatform.repository.AdminRepository;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.repository.PersonProfileRepository;
import com.example.farajaplatform.security.JWTGenerator;
import com.example.farajaplatform.service.*;
import com.example.farajaplatform.service.FileUploaderService;
import com.example.farajaplatform.service.MapperService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthController {
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    PersonRepository personRepository;
    Person person;

    @Autowired
    AdminService adminService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    PersonService personService;
    @Autowired
    PersonProfileService personProfileService;

    @PostMapping("/api/v1/personregister")
    public ResponseEntity<PersonRegistrationResponseDto> registerPerson(@Valid @RequestPart("data") String data,
                                                               @RequestPart("file") MultipartFile file) throws IOException {
        return personService.registerPerson(data, file);

    }

    @PostMapping("api/v1/personlogin")
    public ResponseEntity<PersonLoginResponseDto> PersonLogin(@RequestBody PersonLoginDto personLoginDto) {

            return personService.personLogin(personLoginDto);

        }

    @PutMapping("/api/v1/updateperson/{id}")
    public ResponseEntity<SuccessandMessageDto> updatePerson(@PathVariable Integer id, @RequestBody PersonUpdate personUpdate) {

        return personService.updatePerson(id, personUpdate);

    }

    @PutMapping("/api/v1/updatepersonpassword/{id}")
    public ResponseEntity<SuccessandMessageDto> updatePersonPassword(@PathVariable Integer id, @RequestBody PersonPasswordUpdate personPasswordUpdate) {
        return personService.updatePersonPassword(id, personPasswordUpdate);

    }


        //todo Mpesa endpoints


    @PostMapping("/api/v1/personlogout")
    public ResponseEntity<SuccessandMessageDto> personLogout(HttpServletRequest request) {
      String authorizationHeader = request.getHeader("Authorization");

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

          String token = authorizationHeader.substring(7);

          SuccessandMessageDto response = new SuccessandMessageDto();
          response.setMessage("Logout successful");
          response.setStatus(200);
          return new ResponseEntity<>(response, HttpStatus.OK);
      }
        SuccessandMessageDto response = new SuccessandMessageDto();
        response.setMessage("Invalid request");
        response.setStatus(403);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
