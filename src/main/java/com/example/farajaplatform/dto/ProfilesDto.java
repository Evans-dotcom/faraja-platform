package com.example.farajaplatform.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProfilesDto {
    private int id;
    private String title;
    private String briefDescription;
    private String county;
    private String subcounty;
    private Integer Amount;
    private String date;
    private String email;
    private Integer phoneNo;
    private String FbAccount;
    private String TwitterAccount;
    private String fileName;
}
