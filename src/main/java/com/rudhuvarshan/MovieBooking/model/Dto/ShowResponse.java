package com.rudhuvarshan.MovieBooking.model.Dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class ShowResponse {

    private int showId;

    private LocalDate showDate;

    private LocalTime startTime;

    private String screenName;

    private String theatreName;
}