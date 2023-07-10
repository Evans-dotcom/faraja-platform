package com.example.farajaplatform.controller;

import com.example.farajaplatform.dto.*;
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
    @Autowired
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
    @Autowired
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
                                                               @RequestHeader(name="Authorization") String token) throws IOException{
        SuccessandMessageDto response = new SuccessandMessageDto();
        if(personRepository.existsByEmail(person.getEmail())) {
            response.setMessage("Email is already registered !!");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.BAD_REQUEST);
        }
        person = new Person();
        person.setFirstName(person.getFirstName());
        person.setLastName(person.getLastName());
        person.setEmail(person.getEmail());
        person.setPassword(passwordEncoder.encode(person.getPassword()));
        person.setFileName(person.getFileName());
        person.setStatus(true);
        try {
            person.setCreatedBy(adminRepository.findByUsername(jwtGenerator.getUsernameFromJWT(token.substring(7))).orElseThrow());
        } catch(Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            response.setMessage("Unauthorized request");
            response.setSuccess(false);
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.UNAUTHORIZED);
        }
        personRepository.save(person);
        response.setMessage("Member Registered Successfully !!");
        response.setSuccess(true);
        return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);
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
    public ResponseEntity<SuccessandMessageDto> registerPerson(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file)  {
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


    @PostMapping("/api/v1/widowRegister")
    public ResponseEntity<SuccessandMessageDto> registerWidowProfile(@Valid @RequestPart("data") String data, @RequestPart("file") MultipartFile file) throws IOException {
        try {
            SuccessandMessageDto response = new SuccessandMessageDto();
            WidowProfile widowProfile = mapperService.mapForm(data, WidowProfile.class);
            widowProfile.setFileName(imageUploaderService.uploadImage(file));
            widowService.registerWidowProfile(widowProfile);
            response.setMessage("Profile Created  Successfully !!");
            response.setSuccess(true);
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);
        } catch (ProfileAlreadyExistsException ex) {
            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("NationalID Already In Use");
            response.setSuccess(true);
            return new ResponseEntity<SuccessandMessageDto>(response, HttpStatus.OK);
        }
    }
//    }@GetMapping
//    public ResponseEntity<List<Customer>> findAllCustomers() {
//        return ResponseEntity.ok(CUSTOMERS);
//    }

    //todo load more profiles in Db
    //todo logout endpoint
    //todo Mpesa endpoints
    //todo admin to create personProfile
    //todo email verification

}
