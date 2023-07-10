package com.example.farajaplatform;

import com.example.farajaplatform.model.Person;
import com.example.farajaplatform.model.WidowProfile;
import com.example.farajaplatform.repository.AdminRepository;
import com.example.farajaplatform.security.SecurityConfig;
import com.example.farajaplatform.service.CustomUserDetailsService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FarajaPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(FarajaPlatformApplication.class, args);
	}
	@Bean
	Person setPerson(){
		return new Person();
	}

	@Bean
	WidowProfile setWidowProfile(){
		return new WidowProfile();
	}
//	@Bean
//	SecurityConfig setSecurityConfig(){
//		return new SecurityConfig();
//	}
//	@Bean
//	CustomUserDetailsService setCustomUserDetailsService(){
//		return new CustomUserDetailsService();
//	}

}
