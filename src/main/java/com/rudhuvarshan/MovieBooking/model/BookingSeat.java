package com.rudhuvarshan.MovieBooking.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class BookingSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int bookingSeatId;

    @ManyToOne
    @JoinColumn(name = "booking_id")
    private Bookings booking;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
}