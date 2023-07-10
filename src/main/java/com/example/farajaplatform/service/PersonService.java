package com.example.farajaplatform.service;

import com.example.farajaplatform.exception.UserAlreadyExistsException;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.security.SecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    Person person;
    @Autowired
    SecurityConfig securityConfig;

    public Person registerPerson(Person person) throws UserAlreadyExistsException {

            if (personRepository.findByEmailIgnoreCase(person.getEmail()).isPresent()) {
                throw new UserAlreadyExistsException();
            }
            person.setPassword(securityConfig.passwordEncoder().encode(person.getPassword()));
            person.setStatus(true);
            return personRepository.save(person);

        }
}
