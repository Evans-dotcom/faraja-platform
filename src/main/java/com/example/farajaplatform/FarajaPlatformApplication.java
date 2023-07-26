package com.example.farajaplatform;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class FarajaPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarajaPlatformApplication.class, args);
	}

}
