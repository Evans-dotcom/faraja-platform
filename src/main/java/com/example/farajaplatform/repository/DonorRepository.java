package com.example.farajaplatform.repository;

import com.example.farajaplatform.dto.Donor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonorRepository extends JpaRepository<Donor,Integer> {
}
