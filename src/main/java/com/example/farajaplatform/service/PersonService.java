package com.example.farajaplatform.service;

import com.example.farajaplatform.dto.*;
import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.UserType;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.security.JWTGenerator;
import org.springframework.security.authentication.AuthenticationManager;
import com.example.farajaplatform.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;
    Person person;
    @Autowired
    MapperService mapperService;
    @Autowired
    FileUploaderService fileUploaderService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JWTGenerator jwtGenerator;
    @Autowired
    CustomUserDetailsService customUserDetailsService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    SecurityConfig securityConfig;

    //PERSON REGISTRATION SERVICE
    public ResponseEntity<PersonRegistrationResponseDto> registerPerson(String data, MultipartFile file) throws IOException {
        PersonRegistrationResponseDto response = new PersonRegistrationResponseDto();
        Person person = mapperService.mapForm(data, Person.class);
        person.setFileName(fileUploaderService.uploadFile(file));

        if (personRepository.existsByEmail(person.getEmail())) {
            response.setMessage("Email Already Taken");
            response.setStatus(409);
            return new ResponseEntity<PersonRegistrationResponseDto>(response, HttpStatus.CONFLICT);
        }

        person.setPassword(securityConfig.passwordEncoder().encode(person.getPassword()));
        person.setStatus(true);
        personRepository.save(person);

        response.setMessage("Member Registered Successfully!!");
        response.setStatus(200);
        response.setPerson(person.getId(), person.getFirstName(), person.getLastName(), person.getEmail(), person.getPassword(),person.getFileName());
        return new ResponseEntity<PersonRegistrationResponseDto>(response, HttpStatus.OK);
    }
//PERSON LOGIN SERVICE
    public ResponseEntity<PersonLoginResponseDto> personLogin(PersonLoginDto personLoginDto) {
        PersonLoginResponseDto responseDto = new PersonLoginResponseDto();
        try {
            customUserDetailsService.setUserType(UserType.PERSON);
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(personLoginDto.getEmail(), personLoginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = jwtGenerator.generateToken(authentication, UserType.PERSON.toString());

            Person person = personRepository.findByEmailIgnoreCase(personLoginDto.getEmail())
                    .orElseThrow(() -> new PersonNotFoundException("User not found!"));

            responseDto.setStatus(200);
            responseDto.setMessage("Login successful!");
            responseDto.setToken(token);
            responseDto.setPerson(person.getId(), person.getFirstName(), person.getLastName(), person.getEmail(), person.getPassword(),person.getFileName());

            return new ResponseEntity<>(responseDto, HttpStatus.OK);
        } catch (AuthenticationException | PersonNotFoundException e) {
            responseDto.setStatus(401);
            responseDto.setMessage("Invalid credentials!");
            return new ResponseEntity<>(responseDto, HttpStatus.UNAUTHORIZED);
        }
    }

//UPDATEPERSON SERVICE
    public ResponseEntity<SuccessandMessageDto> updatePerson(Integer id, PersonUpdate personUpdate) {
        SuccessandMessageDto response = new SuccessandMessageDto();
        if (!(personRepository.existsById(id))) {
            response.setMessage("Person does Not exist");
            response.setStatus(403);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Optional<Person> optionalPerson = personRepository.findById(id);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setFirstName(personUpdate.getFirstName());
            person.setLastName(personUpdate.getLastName());
            person.setEmail(personUpdate.getEmail());
            person.setPassword(passwordEncoder.encode(person.getPassword()));
            person.setStatus(true);
            personRepository.save(person);

            response.setMessage("Person updated successfully");
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            // This will handle the case when the person exists in the check above but is not found in findById
            response.setMessage("Person not found");
            response.setStatus(404);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    // UPDATE/CHANGE PERSON PASSWORD
    public ResponseEntity<SuccessandMessageDto> updatePersonPassword(Integer id, PersonPasswordUpdate personPasswordUpdate) {
        SuccessandMessageDto response = new SuccessandMessageDto();
        Optional<Person> optionalPerson = personRepository.findById(id);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            person.setPassword(passwordEncoder.encode(personPasswordUpdate.getPassword()));
            person.setStatus(true);
            personRepository.save(person);

            response.setMessage("Person Password updated successfully");
            response.setStatus(200);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            response.setMessage("Person not found");
            response.setStatus(404);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    public void deletePerson(Integer personId) throws PersonNotFoundException {
        Optional<Person> optionalPerson = personRepository.findById(personId);
        if (optionalPerson.isEmpty()) {
            throw new PersonNotFoundException("User not found!");
        }
        personRepository.deleteById(personId);
    }

    public Person getPersonById(Integer id) {
        Optional<Person> optionalPerson = personRepository.findById(id);
        return optionalPerson.orElse(null);    }

    public void registerPerson(Person person) {
    }
}
