package com.example.farajaplatform.service;

import com.example.farajaplatform.dto.*;
import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Admin;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.UserType;
import com.example.farajaplatform.model.WidowProfile;
import com.example.farajaplatform.repository.AdminRepository;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.security.JWTGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    PersonRepository personRepository;
    Person person;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    AuthenticationManager authenticationManager;
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
    public SuccessandMessageDto registerWidowProfile(String data, MultipartFile file, String token) throws IOException, UserAlreadyExistsException {
        SuccessandMessageDto response = new SuccessandMessageDto();
        WidowProfile widowProfile = mapperService.mapForm(data, WidowProfile.class);
        widowProfile.setFileName(imageUploaderService.uploadImage(file));
        widowService.registerWidowProfile(widowProfile);
        System.out.println(imageUploaderService.uploadImage(file));
        response.setMessage("Profile Created Successfully !!");
        response.setStatus(200);
        widowProfile.setCreatedBy(adminRepository.findByUsername(jwtGenerator.getUsernameFromJWT(token.substring(7))).orElseThrow());
        return response;
    }


    public AdminLoginResponseDto login(AdminDto adminDto) {
        AdminLoginResponseDto responseDto = new AdminLoginResponseDto();
        responseDto.setStatus(200);
        responseDto.setMessage("login successful !!");

        // Perform authentication and generate token
        customUserDetailsService.setUserType(UserType.ADMIN);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(adminDto.getUsername(), adminDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication, UserType.ADMIN.toString());
        responseDto.setToken(token);

        // Fetch the admin details and set them in the response
        Admin admin = adminRepository.findByUsername(adminDto.getUsername()).orElseThrow();
        responseDto.setAdmin(admin.getUsername(), admin.getId());

        return responseDto;
    }
    public ResponseEntity<SuccessandMessageDto> deletePerson(Integer id) {
        try {
            personService.deletePerson(id);

            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Person deleted successfully!");
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PersonNotFoundException ex) {
            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Person not found!");
            response.setStatus(404);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    public AllPersons findAllPersons() {
        List<Person> allPersons = personRepository.findAll();
        AllPersons personDetails = new AllPersons();
        List<UserDetails> userDetails = new ArrayList<>();

        if (allPersons.size() > 0) {
            personDetails.setStatus(200);
            personDetails.setMessage("All Persons found");
            UserDetails allUserDetails;
            for (Person person : allPersons) {
                allUserDetails = new UserDetails();
                allUserDetails.setId(person.getId());
                allUserDetails.setEmail(person.getEmail());
                allUserDetails.setFirstName(person.getFirstName());
                allUserDetails.setLastName(person.getLastName());
                allUserDetails.setPassword(person.getPassword());
                userDetails.add(allUserDetails);
            }
            personDetails.setPersons((ArrayList<UserDetails>) userDetails);
        } else {
            personDetails.setStatus(403);
            personDetails.setMessage("Empty List");
        }

        return personDetails;
    }
}
