package com.rudhuvarshan.MovieBooking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
public class Shows {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int showId;

    private LocalDate showDate;

    private LocalTime startTime;

    @ManyToOne
    private Screen screen;

    @ManyToOne
    private Movie movie;
}