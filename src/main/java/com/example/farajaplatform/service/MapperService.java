package com.example.farajaplatform.service;

import com.fasterxml.jackson.databind.json.JsonMapper;
import org.springframework.stereotype.Service;

@Service
public class MapperService {

    //can be used for any class
    //method for mapping JSON data to a Java object
    public <T> T mapForm(String data, Class<T> classValue) {
        JsonMapper mapper = new JsonMapper();
        T t = null;
        try {
            t = mapper.readValue(data,classValue);
        } catch (Exception e) {
            System.out.println(e);
        }
        return t;
    }
}

