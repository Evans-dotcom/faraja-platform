package com.example.farajaplatform.dto;

import lombok.*;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private Integer id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
