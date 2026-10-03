package com.rudhuvarshan.MovieBooking.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seatId;

    private String seatRow;

    private int seatNum;

    private int seatPrice;

    private String type;

    @JsonIgnore
    @ManyToOne
    private Screen screen;
}