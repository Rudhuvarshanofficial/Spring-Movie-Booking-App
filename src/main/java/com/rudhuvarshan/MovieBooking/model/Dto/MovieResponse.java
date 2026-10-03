package com.rudhuvarshan.MovieBooking.model.Dto;

import lombok.Data;

import java.util.List;

@Data
public class MovieResponse {

    private int movieId;

    private String movieName;

    private String cast;

    private int duration;

    private String language;

    private List<ShowResponse> shows;
}