package com.example.farajaplatform.dto;

import lombok.*;

@Getter
@Setter
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonDetails {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String fileName;
}
