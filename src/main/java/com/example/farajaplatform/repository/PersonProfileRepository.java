package com.example.farajaplatform.repository;

import com.example.farajaplatform.model.WidowProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WidowProfileRepository extends JpaRepository<WidowProfile,Integer> {

    Optional<WidowProfile> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);

    List<WidowProfile> findByTitleContainingIgnoreCaseOrBriefDescriptionContainingIgnoreCase(String titleKeyword, String briefDescriptionKeyword);
}
