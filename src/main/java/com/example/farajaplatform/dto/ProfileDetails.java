package com.example.farajaplatform.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import lombok.*;

@Getter
@Setter
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProfileDetails {

    private String briefDescription;

    private String fileName;

}
