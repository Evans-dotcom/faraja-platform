package com.example.farajaplatform.dto;

import com.example.farajaplatform.model.PersonProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResponse {
    private SuccessandMessageDto response;
    private List<PersonProfile> personProfiles;
}
