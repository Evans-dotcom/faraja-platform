package com.example.farajaplatform.controller;

import com.example.farajaplatform.dto.*;
import com.example.farajaplatform.dto.AllPersonProfiles;
import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.exception.ProfileAlreadyExistsException;
import com.example.farajaplatform.model.PersonProfile;
import com.example.farajaplatform.repository.PersonProfileRepository;
import com.example.farajaplatform.security.JWTGenerator;
import com.example.farajaplatform.service.ImageUploaderService;
import com.example.farajaplatform.service.MapperService;
import com.example.farajaplatform.service.PersonProfileService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
public class ProfileController {
    @Autowired
    PersonProfileRepository personProfileRepository;
    PersonProfile personProfile;
    @Autowired
    PersonProfileService personProfileService;
    @Autowired
    JWTGenerator jwtGenerator;


    @PostMapping("/api/v1/createprofile")
    public ResponseEntity<ProfileCreationResponseDto> registerWidowProfile(@Valid @RequestPart("data") String data,
                                                                           @RequestPart("file") MultipartFile file) throws IOException {

        return personProfileService.registerWidowProfile(data, file);
    }

    @PostMapping("/api/public/searchpersonprofile")
    public ResponseEntity<SearchResponse> searchWidowProfile(
            @RequestParam("briefDescription") String briefDescription) {
        List<PersonProfile> personProfiles = personProfileService.findWidowProfileByKeyword(briefDescription);

        if (personProfiles.isEmpty()) {
            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("No Profile found");
            response.setStatus(204);
            return new ResponseEntity<>(new SearchResponse(response, null), HttpStatus.NOT_FOUND);
        } else {
            SuccessandMessageDto response = new SuccessandMessageDto();
            response.setMessage("Profiles found");
            response.setStatus(200);
            return new ResponseEntity<>(new SearchResponse(response, personProfiles), HttpStatus.OK);
        }
    }

    @PostMapping("/api/public/listprofiles")
    public AllPersonProfiles findAllPersonProfiles() {
        return personProfileService.findAllPersonProfiles();
    }

}
