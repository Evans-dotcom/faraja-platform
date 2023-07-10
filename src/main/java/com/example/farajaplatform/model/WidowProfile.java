package com.example.farajaplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.context.annotation.Primary;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person_profile")
public class WidowProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="title",length = 50)
    private String title;

    @Column(name = "BriefDescription",length = 3000,nullable = false)
    private String BriefDescription;
    private String county;

    private String subcounty;

    private Integer Amount;

    private String date;
    @Email
    private String email;

    private Integer phoneNo;

    private String FbAccount;
    private String TwitterAccount;
    private String fileName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, mappedBy = "widowProfile")
    private Set<Person> persons = new HashSet<>();



    public WidowProfile() {
    }

    public WidowProfile(String title, String briefDescription, Integer nationalID, String county, String subcounty, Integer amount, String date, String email,
                        Integer phoneNo, String fbAccount, String twitterAccount, String fileName) {
        this.title = title;
        BriefDescription = briefDescription;
        this.county = county;
        this.subcounty = subcounty;
        Amount = amount;
        this.date = date;
        this.email = email;
        this.phoneNo = phoneNo;
        FbAccount = fbAccount;
        TwitterAccount = twitterAccount;
        this.fileName = fileName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBriefDescription() {
        return BriefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        BriefDescription = briefDescription;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getSubcounty() {
        return subcounty;
    }

    public void setSubcounty(String subcounty) {
        this.subcounty = subcounty;
    }

    public Integer getAmount() {
        return Amount;
    }

    public void setAmount(Integer amount) {
        Amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(Integer phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getFbAccount() {
        return FbAccount;
    }

    public void setFbAccount(String fbAccount) {
        FbAccount = fbAccount;
    }

    public String getTwitterAccount() {
        return TwitterAccount;
    }

    public void setTwitterAccount(String twitterAccount) {
        TwitterAccount = twitterAccount;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Set<Person> getPersons() {
        return persons;
    }

    public void setPersons(Set<Person> persons) {
        this.persons = persons;
    }
}
