package com.rudhuvarshan.MovieBooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Movie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int movieId;

    private String movieName;

    private String cast;

    private int duration;

    private String language;

    @OneToMany(mappedBy = "movie")
    private List<Shows> shows;
}