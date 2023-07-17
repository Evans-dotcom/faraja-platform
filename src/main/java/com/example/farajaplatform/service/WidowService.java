package com.example.farajaplatform.service;

import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.exception.ProfileAlreadyExistsException;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.WidowProfile;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.repository.WidowProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WidowService {

    WidowProfile widowProfile;
    Person person;
    @Autowired
    PersonRepository personRepository;
    @Autowired
    WidowProfileRepository widowProfileRepository;

    public WidowProfile registerWidowProfile(String email,WidowProfile widowProfile) throws ProfileAlreadyExistsException, PersonNotFoundException {
        Person person =personRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new PersonNotFoundException());

        if (widowProfileRepository.findByEmailIgnoreCase(widowProfile.getEmail()).isPresent()){
            throw new ProfileAlreadyExistsException();
        }

        widowProfile.getPersons().add(person);
        person.setWidowProfile(widowProfile);
        return widowProfileRepository.save(widowProfile);
    }

    public void registerWidowProfile(WidowProfile widowProfile) {
    }
}
