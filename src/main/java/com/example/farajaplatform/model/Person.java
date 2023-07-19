package com.example.farajaplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    private String firstName;
    private String lastName;
    @Email
    private String email;
    private String password;
    private String fileName;
    private boolean status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "widowProfile_id")
    private WidowProfile widowProfile;

    @ManyToOne
    @JoinColumn(name="createdBy", referencedColumnName = "id")
    private Admin createdBy;

    

}
