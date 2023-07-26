package com.example.farajaplatform.dto;

import lombok.*;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
class AdminDetails {
    String username;
    int id;

}

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class AdminLoginResponseDto {
    private Integer status;
    private String message;
    private String token;
    private AdminDetails admin;

    public void setAdmin(String username, int id) {
        this.admin = AdminDetails.builder()
                .username(username)
                .id(id)
                .build();
    }
}
