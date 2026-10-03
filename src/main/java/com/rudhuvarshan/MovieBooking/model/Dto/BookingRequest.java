package com.rudhuvarshan.MovieBooking.model.Dto;

import lombok.Data;

import java.util.List;

@Data
public class BookingRequest {

    private int showId;

    private List<Integer> seatIds;
}