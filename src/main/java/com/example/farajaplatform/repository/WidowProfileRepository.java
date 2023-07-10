package com.example.farajaplatform.repository;

import com.example.farajaplatform.model.WidowProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WidowProfileRepository extends JpaRepository<WidowProfile,Integer> {

    Optional<WidowProfile> findByNationalID(Integer nationalID);
    boolean existsByNationalID(Integer nationalID);

}
