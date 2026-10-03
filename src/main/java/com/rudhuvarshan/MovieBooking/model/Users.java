package com.rudhuvarshan.MovieBooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int userId;

    private String fullName;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private String phoneNumber;

    private String location;

    private String role;

    public Users() {
    }

    @JsonIgnore
    @OneToOne(mappedBy = "manager")
    private Theatre theatres;

    public Users(String mail, String encode, String admin) {
        email = mail;
        password = encode;
        role = admin;
    }
}