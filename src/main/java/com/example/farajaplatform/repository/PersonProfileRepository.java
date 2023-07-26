package com.example.farajaplatform.repository;

import com.example.farajaplatform.model.PersonProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface PersonProfileRepository extends JpaRepository<PersonProfile,Integer> {

    Optional<PersonProfile> findByEmailIgnoreCase(String email);
    boolean existsByEmail(String email);

    void deleteByEmail(String email);


    List<PersonProfile> findByBriefDescriptionContainingIgnoreCase(String briefDescriptionKeyword);
}
