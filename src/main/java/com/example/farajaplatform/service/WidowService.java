package com.example.farajaplatform.service;

import com.example.farajaplatform.exception.ProfileAlreadyExistsException;
import com.example.farajaplatform.model.WidowProfile;
import com.example.farajaplatform.repository.WidowProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WidowService {

    @Autowired
    WidowProfile widowProfile;
    @Autowired
    WidowProfileRepository widowProfileRepository;

    public WidowProfile registerWidowProfile(WidowProfile widowProfile) throws ProfileAlreadyExistsException{
        if (widowProfileRepository.findByEmailIgnoreCase(widowProfile.getEmail()).isPresent()){
            throw new ProfileAlreadyExistsException();
        }
        return widowProfileRepository.save(widowProfile);
    }
}
