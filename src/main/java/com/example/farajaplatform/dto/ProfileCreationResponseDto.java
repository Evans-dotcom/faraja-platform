package com.example.farajaplatform.dto;

import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.PersonProfile;
import lombok.Data;

@Data
public class ProfileCreationResponseDto {
    private int status;
    private String message;
    ProfilesDto profilesDto;

    public void setPersonProfile(Integer id, String title, String briefDescription, String county, String subcounty,int Amount,
                                 String date,String email,int phoneNo,String FbAccount,String TwitterAccount,String fileName) {
        this.profilesDto= ProfilesDto.builder()
                .title(title)
                .briefDescription(briefDescription)
                .county(county)
                .subcounty(subcounty)
                .Amount(Amount)
                .date(date)
                .email(email)
                .phoneNo(phoneNo)
                .FbAccount(FbAccount)
                .TwitterAccount(TwitterAccount)
                .fileName(fileName)
                .id(id)
                .build();

    }

}
