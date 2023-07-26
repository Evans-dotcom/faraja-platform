package com.example.farajaplatform.service;

import com.example.farajaplatform.dto.*;
import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Admin;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.PersonProfile;
import com.example.farajaplatform.model.UserType;
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
    PersonProfileService personProfileService;
    PersonProfile personProfile;

    public boolean isAdminUsernameTaken(String username) {
        return adminRepository.existsByUsername(username);
    }

    public void registerAdmin(String username, String password) {
        Admin admin = new Admin();
        admin.setUsername(username);
        admin.setPassword(passwordEncoder.encode(password));
        adminRepository.save(admin);
    }
    public void registerPerson(String data, MultipartFile file, String token) throws UserAlreadyExistsException, IOException {
        Person person = mapperService.mapForm(data, Person.class);
        person.setFileName(fileUploaderService.uploadFile(file));
        personService.registerPerson(person);

        String adminUsername = jwtGenerator.getUsernameFromJWT(token.substring(7));
        person.setCreatedBy(adminRepository.findByUsername(adminUsername).orElseThrow());
    }
    public SuccessandMessageDto registerWidowProfile(String data, MultipartFile file, String token) throws IOException, UserAlreadyExistsException {
        SuccessandMessageDto response = new SuccessandMessageDto();
        PersonProfile personProfile = mapperService.mapForm(data, PersonProfile.class);
        personProfile.setFileName(imageUploaderService.uploadImage(file));
        personProfileService.registerWidowProfile(personProfile);
        System.out.println(imageUploaderService.uploadImage(file));
        response.setMessage("Profile Created Successfully !!");
        response.setStatus(200);
        personProfile.setCreatedBy(adminRepository.findByUsername(jwtGenerator.getUsernameFromJWT(token.substring(7))).orElseThrow());
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
            Person person = personService.getPersonById(id);

            if (person == null) {
                SuccessandMessageDto response = new SuccessandMessageDto();
                response.setMessage("Person not found!");
                response.setStatus(404);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            String personEmail = person.getEmail();

            // Check if the email is associated with any PersonProfile
            List<PersonProfile> personProfiles = personProfileService.findPersonProfileByEmail(personEmail);
            if (personProfiles != null && !personProfiles.isEmpty()) {
                // Delete the corresponding PersonProfiles based on the Person's email
                for (PersonProfile profile : personProfiles) {
                    personProfileService.deletePersonProfileByEmail(profile.getEmail());
                }
            }

            // Delete the Person
            personService.deletePerson(id);

            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Person and associated PersonProfile deleted successfully!");
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PersonNotFoundException ex) {
            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Person not found!");
            response.setStatus(404);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }


//    public ResponseEntity<SuccessandMessageDto> deletePerson(Integer id) {
//        try {
//
//            Person person = personService.getPersonById(id);
//            String personEmail = person.getEmail();
//
//            personProfileService.deletePersonProfileByEmail(personEmail);
//
//            personService.deletePerson(id);
//
//            SuccessandMessageDto response = new SuccessandMessageDto();
//            response.setMessage("Person deleted successfully!");
//            response.setStatus(200);
//            return new ResponseEntity<>(response, HttpStatus.OK);
//        } catch (PersonNotFoundException ex) {
//            SuccessandMessageDto response = new SuccessandMessageDto();
//            response.setMessage("Person not found!");
//            response.setStatus(404);
//            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
//        }
//    }

    public AllPersons findAllPersons() {
        List<Person> allPersons = personRepository.findAll();
        AllPersons personDetails = new AllPersons();
        ArrayList<UserDetails> userDetails = new ArrayList<>();

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
            personDetails.setPersons(userDetails);
        } else {
            personDetails.setStatus(403);
            personDetails.setMessage("Empty List");
        }

        return personDetails;
    }
}
