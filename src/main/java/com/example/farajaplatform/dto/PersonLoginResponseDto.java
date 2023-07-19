package com.example.farajaplatform.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
class PersonDetails {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
@Getter
@Setter
public class PersonLoginResponseDto {
    private Integer status;
    private String message;
    private String token;
    private PersonDetails person;


    public void setPerson(Integer id, String firstName, String lastName, String email, String password) {
    }
}
