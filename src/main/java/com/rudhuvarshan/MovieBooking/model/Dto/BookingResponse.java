package com.rudhuvarshan.MovieBooking.model.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Data
public class BookingResponse {

    private int bookingId;

    private String movieName;

    private String theatreName;

    private String screenName;

    private LocalDate showDate;

    private LocalTime startTime;

    private List<Integer> seatIds;

    private double totalAmount;

    private LocalDateTime bookingTime;

    private String status;
}