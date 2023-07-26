package com.example.farajaplatform.dto;

import com.example.farajaplatform.model.Person;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonLoginResponseDto {
    private Integer status;
    private String message;
    private String token;
    PersonDetails person;


    public void setPerson(Integer id, String firstName, String lastName, String email, String password,String fileName) {
        this.person = PersonDetails.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .password(password)
                .fileName(fileName)
                .id(id)
                .build();
    }
}
