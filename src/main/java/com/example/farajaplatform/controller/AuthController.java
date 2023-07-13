package com.example.farajaplatform.controller;

import com.example.farajaplatform.dto.*;
import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.exception.ProfileAlreadyExistsException;
import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Admin;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.UserType;
import com.example.farajaplatform.model.WidowProfile;
import com.example.farajaplatform.repository.AdminRepository;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.security.JWTGenerator;
import com.example.farajaplatform.service.*;
import com.example.farajaplatform.service.FileUploaderService;
import com.example.farajaplatform.service.MapperService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
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
    AuthenticationManager authenticationManager;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
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

    @PostMapping("api/v1/adminRegister")
    public ResponseEntity<String> adminRegister(@RequestBody AdminDto adminDto) {
        if(adminRepository.existsByUsername(adminDto.getUsername())) {
            return new ResponseEntity<String>("Username is taken !! ", HttpStatus.BAD_REQUEST);
        }
        Admin admin = new Admin();
        admin.setUsername(adminDto.getUsername());
        admin.setPassword(passwordEncoder.encode(adminDto.getPassword()));

        adminRepository.save(admin);
        return new ResponseEntity<String>("Admin Registered successfully !! ", HttpStatus.CREATED);
    }

    @PostMapping("/api/v1/admin/personRegister")
    public ResponseEntity<SuccessandMessageDto> registerPerson(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file,
                                                               @RequestHeader(name="Authorization") String token) throws IOException, UserAlreadyExistsException {

        SuccessandMessageDto response = new SuccessandMessageDto();
        try{
            Person person = mapperService.mapForm(data, Person.class);
            person.setFileName(fileUploaderService.uploadFile(file));
            personService.registerPerson(person);
            System.out.println(fileUploaderService.uploadFile(file));
            response.setMessage("Member Registered Successfully !!");
            response.setSuccess(true);
            person.setCreatedBy(adminRepository.findByUsername(jwtGenerator.getUsernameFromJWT(token.substring(7))).orElseThrow());
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);

        }catch (UserAlreadyExistsException ex) {
            response.setMessage("Email Already Taken");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response,HttpStatus.CONFLICT);
        }
    }
    @PostMapping("/api/v1/admin/personProfile")
    public ResponseEntity<SuccessandMessageDto> registerWidowProfile(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file,
                                                               @RequestHeader(name="Authorization") String token) throws IOException, UserAlreadyExistsException {

        SuccessandMessageDto response = new SuccessandMessageDto();
        try{
            WidowProfile widowProfile = mapperService.mapForm(data, WidowProfile.class);
            widowProfile.setFileName(imageUploaderService.uploadImage(file));
            widowService.registerWidowProfile(widowProfile);
            System.out.println(imageUploaderService.uploadImage(file));
            response.setMessage("Profile Created  Successfully !!");
            response.setSuccess(true);
            person.setCreatedBy(adminRepository.findByUsername(jwtGenerator.getUsernameFromJWT(token.substring(7))).orElseThrow());
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);

        }catch (ProfileAlreadyExistsException ex) {
            response.setMessage("Email Already In Use");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response,HttpStatus.CONFLICT);
        }
    }

    @PostMapping("api/v1/adminLogin")
    public ResponseEntity<AdminLoginResponseDto> login(@RequestBody AdminDto adminDto) {
        System.out.println("adminLogin");
        customUserDetailsService.setUserType(UserType.ADMIN);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminDto.getUsername(), adminDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtGenerator.generateToken(authentication,UserType.ADMIN.toString());
        AdminLoginResponseDto responseDto = new AdminLoginResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("login successful !!");
        responseDto.setToken(token);
        Admin admin = adminRepository.findByUsername(adminDto.getUsername()).orElseThrow();
        responseDto.setAdmin(admin.getUsername(), admin.getId());
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/api/v1/personRegister")
    public ResponseEntity<SuccessandMessageDto> registerPerson(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file) throws IOException {

        try {
            SuccessandMessageDto response = new SuccessandMessageDto();
            Person person = mapperService.mapForm(data, Person.class);
            person.setFileName(fileUploaderService.uploadFile(file));
            personService.registerPerson(person);
            System.out.println(fileUploaderService.uploadFile(file));
            response.setMessage("Member Registered Successfully !!");
            response.setSuccess(true);
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);

    }catch (UserAlreadyExistsException ex) {
            SuccessandMessageDto response=new SuccessandMessageDto();
            response.setMessage("Email Already Taken");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response,HttpStatus.CONFLICT);
        }
    }

    @PostMapping("api/v1/personLogin")
    public ResponseEntity<PersonLoginResponseDto> PersonLogin(@RequestBody PersonLoginDto personLoginDto) {
        System.out.println("personLogin");
        customUserDetailsService.setUserType(UserType.PERSON);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(personLoginDto.getEmail(), personLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, UserType.PERSON.toString());
        PersonLoginResponseDto responseDto = new PersonLoginResponseDto();
        responseDto.setSuccess(true);
        responseDto.setMessage("login successful !!");
        responseDto.setToken(token);
       Person person = personRepository.findByEmailIgnoreCase(personLoginDto.getEmail()).orElseThrow();
        responseDto.setPerson(person.getId(),person.getFirstName(),person.getLastName(), person.getEmail(), person.getPassword());
        return new ResponseEntity<PersonLoginResponseDto>(responseDto, HttpStatus.OK);
    }

    @PutMapping("/api/v1/updatePerson/{id}")
    public ResponseEntity<SuccessandMessageDto>updatePerson(@PathVariable Integer id,@RequestBody PersonUpdate personUpdate){
        System.out.println("personUpdate");
        SuccessandMessageDto response=new SuccessandMessageDto();
        if (!(personRepository.existsById(id))){
            response.setMessage("Person does Not exist");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response,
                    HttpStatus.BAD_REQUEST);
        }
        Optional<Person> Person = personRepository.findById(id);
        Person person = Person.get();
        person.setFirstName(personUpdate.getFirstName());
        person.setLastName(personUpdate.getLastName());
        person.setEmail(personUpdate.getEmail());
        person.setPassword(passwordEncoder.encode(person.getPassword()));

        person.setStatus(true);
        personRepository.save(person);
        response.setMessage("Person updated successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessandMessageDto>(response,HttpStatus.OK);
    }

    @PutMapping("/api/v1/updatePersonPassword/{id}")
    public ResponseEntity<SuccessandMessageDto>updatePersonPassword(@PathVariable Integer id,@RequestBody PersonPasswordUpdate personPasswordUpdate){
        System.out.println("personPasswordUpdate");
        SuccessandMessageDto response=new SuccessandMessageDto();
        if (!(personRepository.existsById(id))){
            response.setMessage("Person does Not exist");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response,
                    HttpStatus.BAD_REQUEST);
        }
        Optional<Person> Person = personRepository.findById(id);
        Person person = Person.get();
        person.setPassword(passwordEncoder.encode(personPasswordUpdate.getPassword()));

        person.setStatus(true);
        personRepository.save(person);
        response.setMessage("Person Password updated successfully");
        response.setSuccess(true);
        return new ResponseEntity<SuccessandMessageDto>(response,HttpStatus.OK);
    }
    @DeleteMapping("/api/v1/personDelete/{id}")
    public ResponseEntity<SuccessandMessageDto> deletePerson(@PathVariable("id") Integer id) {
        try {
            personService.deletePerson(id);

            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Person deleted successfully!");
            response.setSuccess(true);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PersonNotFoundException ex) {
            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Person not found!");
            response.setSuccess(false);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    @GetMapping("/api/v1/view")
    public List<Person> findAllPerson(){
        return personRepository.findAll();
    }


    @PostMapping("/api/v1/widowRegister")
    public ResponseEntity<SuccessandMessageDto> registerWidowProfile(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file) throws IOException {
        try {
            SuccessandMessageDto response = new SuccessandMessageDto();
            WidowProfile widowProfile = mapperService.mapForm(data, WidowProfile.class);
            widowProfile.setFileName(imageUploaderService.uploadImage(file));
            widowService.registerWidowProfile(widowProfile);
            System.out.println(imageUploaderService.uploadImage(file));
            response.setMessage("Profile Created  Successfully !!");
            response.setSuccess(true);
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);

        } catch (ProfileAlreadyExistsException ex) {
            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Email Already In Use");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);
        }
    }


    //todo load more profiles in Db
    //todo Mpesa endpoints
    //todo email verification

//    @GetMapping("/api/v1/pics")
//    public List<uploads> uploads(){
//        return uploads.get();
//    }

    @PostMapping("/api/v1/personLogout")
    public ResponseEntity<SuccessandMessageDto> personLogout(HttpServletRequest request) {
      String authorizationHeader = request.getHeader("Authorization");

      if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

          String token = authorizationHeader.substring(7);

          SuccessandMessageDto response = new SuccessandMessageDto();
          response.setMessage("Logout successful");
          response.setSuccess(true);
          return new ResponseEntity<>(response, HttpStatus.OK);
      }
        SuccessandMessageDto response = new SuccessandMessageDto();
        response.setMessage("Invalid request");
        response.setSuccess(false);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
