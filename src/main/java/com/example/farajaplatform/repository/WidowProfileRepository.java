package com.example.farajaplatform.repository;

import com.example.farajaplatform.model.WidowProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WidowProfileRepository extends JpaRepository<WidowProfile,Integer> {

    Optional<WidowProfile> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);

}
