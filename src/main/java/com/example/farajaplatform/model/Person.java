package com.example.farajaplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

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
    @JoinColumn(name = "personProfile_id")
    private PersonProfile personProfile;

    @ManyToOne
    @JoinColumn(name="createdBy", referencedColumnName = "id")
    private Admin createdBy;

}
