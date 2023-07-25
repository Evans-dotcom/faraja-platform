package com.example.farajaplatform.dto;

import com.example.farajaplatform.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class PersonRegistrationResponseDto {
    private int status;
    private String message;
    private Person PersonDetails;
}
