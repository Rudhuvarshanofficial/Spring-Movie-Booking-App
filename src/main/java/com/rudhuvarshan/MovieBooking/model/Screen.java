package com.rudhuvarshan.MovieBooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Screen {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int screenId;

    private String screenName;

    @OneToMany(mappedBy = "screen")
    private List<Seat> seats;

    @JsonIgnore
    @OneToMany(mappedBy = "screen")
    private List<Shows> shows;

    @ManyToOne
    private Theatre theatre;
}