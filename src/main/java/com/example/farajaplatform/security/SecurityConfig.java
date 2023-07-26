package com.example.farajaplatform.security;


import com.example.farajaplatform.model.UserType;
import com.example.farajaplatform.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Autowired
    JWTAuthEntryPoint jwtAuthEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
//                .cors(Customizer.withDefaults())
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers(request -> request.getRequestURI().equals("/api/public/listprofiles")).permitAll()
                .requestMatchers(request -> request.getRequestURI().equals("/api/public/searchpersonprofile")).permitAll()
                .requestMatchers("/api/public/**").hasAuthority(UserType.PERSON.toString())
                .requestMatchers("/api/v1/adminregister").permitAll()
                .requestMatchers("/api/v1/admin/personregister").permitAll()
                .requestMatchers("/api/v1/personregister").permitAll()
                .requestMatchers("/api/v1/personlogin").permitAll()
                .requestMatchers("/api/v1/adminlogin").permitAll()
                .requestMatchers("/api/v1/createprofile").permitAll()
                .requestMatchers("/images/{filename}").permitAll()
                .requestMatchers("/api/v1/updateperson").permitAll()
                .requestMatchers("/api/v1/updatepersonpassword").permitAll()
                .requestMatchers("/api/v1/persondelete").permitAll()
                /* for any endpoint that has api/v1/admin/** admin has authority over it*/
                .requestMatchers("/api/v1/admin/**").hasAuthority(UserType.ADMIN.toString())
                .anyRequest().authenticated()
                .and()
                .httpBasic();
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

//    @Bean
//    CorsConfigurationSource corsConfiguration(){
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.addAllowedOrigin("*");
//        configuration.addAllowedMethod("*");
//        configuration.addAllowedHeader("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",configuration);
//        return source;
//    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JWTAuthenticationFilter jwtAuthenticationFilter() {
        return new JWTAuthenticationFilter();
    }
}
