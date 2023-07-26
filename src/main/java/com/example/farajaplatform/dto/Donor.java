package com.example.farajaplatform.dto;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "donor")
public class Donor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String fullNames;
    private String email;
    private int phoneNo;
}
