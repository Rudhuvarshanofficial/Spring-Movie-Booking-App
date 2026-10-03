package com.rudhuvarshan.MovieBooking.controller;

import com.rudhuvarshan.MovieBooking.model.Dto.MovieResponse;
import com.rudhuvarshan.MovieBooking.model.Movie;
import com.rudhuvarshan.MovieBooking.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("movie")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @PostMapping("")
    public List<Movie> addMovies(
            @RequestBody List<Movie> movies) {

        return movieService.addMovies(movies);
    }

    @GetMapping("")
    public List<MovieResponse> getAllMovies() {

        return movieService.getAllMovies();
    }

    @PutMapping("/{movieName}")
    public Movie updateMovie(
            @PathVariable String movieName,
            @RequestBody Movie movie) {

        return movieService.updateMovie(movieName, movie);
    }

    @DeleteMapping("{movieName}")
    public String deleteMovie(
            @PathVariable String movieName) {

        return movieService.deleteMovie(movieName);
    }
}