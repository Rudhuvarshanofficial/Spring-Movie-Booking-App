package com.rudhuvarshan.MovieBooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Theatre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int theatreId;

    private String theatreName;

    private String location;

    @JsonIgnore
    @OneToMany(mappedBy = "theatre")
    private List<Screen> screens;

    @JsonIgnore
    @OneToOne
    private Users manager;
}