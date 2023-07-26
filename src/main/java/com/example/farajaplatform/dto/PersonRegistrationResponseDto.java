package com.example.farajaplatform.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class PersonRegistrationResponseDto {
    private int status;
    private String message;
    PersonDetails personDetails;


    public void setPerson(Integer id, String firstName, String lastName, String email, String password, String fileName) {
        this.personDetails = PersonDetails.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .fileName(fileName)
                .id(id)
                .build();
    }

}
