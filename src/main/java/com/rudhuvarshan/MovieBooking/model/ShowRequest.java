package com.rudhuvarshan.MovieBooking.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowRequest {

    private LocalDate showDate;

    private LocalTime startTime;

    private int movieId;

    private int screenId;
}