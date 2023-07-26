package com.example.farajaplatform.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
@Getter
@Setter
public class AllPersonProfiles {
    private Integer status;
    private String message;
    private ArrayList<ProfileDetails> personProfiles;
}
