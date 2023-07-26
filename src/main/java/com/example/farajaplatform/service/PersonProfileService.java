package com.example.farajaplatform.service;

import com.example.farajaplatform.dto.*;
import com.example.farajaplatform.exception.PersonNotFoundException;
import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.PersonProfile;
import com.example.farajaplatform.repository.PersonRepository;
import com.example.farajaplatform.repository.PersonProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PersonProfileService {

    PersonProfile personProfile;
    Person person;
    @Autowired
    PersonRepository personRepository;
   @Autowired
   ImageUploaderService imageUploaderService;
   @Autowired
   MapperService mapperService;
    @Autowired
    PersonProfileRepository personProfileRepository;


    public ResponseEntity<ProfileCreationResponseDto> registerWidowProfile(String data, MultipartFile file) throws IOException {
        ProfileCreationResponseDto response = new ProfileCreationResponseDto();
        PersonProfile personProfile = mapperService.mapForm(data, PersonProfile.class);
        personProfile.setFileName(imageUploaderService.uploadImage(file));

        try {
            Person person = personRepository.findByEmailIgnoreCase(personProfile.getEmail())
                    .orElseThrow(() -> new PersonNotFoundException("User not found!"));

            if (personProfileRepository.findByEmailIgnoreCase(personProfile.getEmail()).isPresent()) {
                response.setMessage("Profile with the same email already exists!");
                response.setStatus(409);
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }

            personProfile.getPersons().add(person);
            person.setPersonProfile(personProfile);
            personProfileRepository.save(personProfile);

            response.setMessage("Profile Created Successfully!");
            response.setStatus(200);
            response.setPersonProfile(
                    personProfile.getId(),
                    personProfile.getTitle(),
                    personProfile.getBriefDescription(),
                    personProfile.getCounty(),
                    personProfile.getSubcounty(),
                    personProfile.getAmount(),
                    personProfile.getDate(),
                    personProfile.getEmail(),
                    personProfile.getPhoneNo(),
                    personProfile.getFbAccount(),
                    personProfile.getTwitterAccount(),
                    personProfile.getFileName());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (PersonNotFoundException e) {
            e.printStackTrace();
            response.setMessage("Error while uploading file");
            response.setStatus(500);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public List<PersonProfile> findWidowProfileByKeyword(String briefDescription) {
        // Query the repository to find PersonProfile any line in BriefDescription
        return personProfileRepository.findByBriefDescriptionContainingIgnoreCase(briefDescription);
    }

    public void registerWidowProfile(PersonProfile personProfile) {
    }

    public AllPersonProfiles findAllPersonProfiles() {
        List<PersonProfile> allPersonProfiles = personProfileRepository.findAll();
        AllPersonProfiles personProfiles = new AllPersonProfiles();
        ArrayList<ProfileDetails> profileDetails = new ArrayList<>();

        if (allPersonProfiles.size() > 0) {
            personProfiles.setStatus(200);
            personProfiles.setMessage("All PersonProfiles found");
            ProfileDetails allProfileDetails;
            for (PersonProfile personProfile : allPersonProfiles) {
                allProfileDetails = new ProfileDetails();
                allProfileDetails.setBriefDescription(personProfile.getBriefDescription());
                allProfileDetails.setFileName(personProfile.getFileName());

                profileDetails.add(allProfileDetails);
            }
            personProfiles .setPersonProfiles(profileDetails);
        } else {
            personProfiles.setStatus(403);
            personProfiles.setMessage("Empty List");
        }

        return personProfiles;
    }

    public void deletePersonProfileByEmail(String email) {
        personProfileRepository.deleteByEmail(email);
        if (personProfile != null) {
            personProfileRepository.delete(personProfile);
        }
    }

    public List<PersonProfile> findPersonProfileByEmail(String personEmail) {
        return null;
    }


}
