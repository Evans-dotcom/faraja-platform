package com.example.farajaplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;



@Entity
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

    @ManyToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JoinColumn(name = "widowProfile_id")
    private WidowProfile widowProfile;
    @ManyToOne
    @JoinColumn(name="createdBy", referencedColumnName = "id")
    private Admin createdBy;



    public Person() {
    }

    public Person(Integer id, String firstName,
                  String lastName, String email, String password, String fileName, boolean status) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.fileName=fileName;
        this.status = status;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFileName() {return fileName;}

    public void setFileName(String fileName) {this.fileName = fileName;}

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Long getId() {
        return null;
    }

    public WidowProfile getWidowProfile() {
        return widowProfile;
    }

    public void setWidowProfile(WidowProfile widowProfile) {
        this.widowProfile = widowProfile;
    }

    public Admin getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Admin createdBy) {
        this.createdBy = createdBy;
    }
}
