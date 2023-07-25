package com.example.farajaplatform.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.context.annotation.Primary;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "person_profile")
public class WidowProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name="title",length = 50)
    private String title;

    @Column(name = "briefDescription",length = 3000,nullable = false)
    private String briefDescription;
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
    @ManyToOne
    @JoinColumn(name="createdBy", referencedColumnName = "id")
    private Admin createdBy;

}
