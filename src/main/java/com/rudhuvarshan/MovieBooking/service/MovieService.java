package com.rudhuvarshan.MovieBooking.service;

import com.rudhuvarshan.MovieBooking.exception.BadRequestException;
import com.rudhuvarshan.MovieBooking.exception.ResourceNotFoundException;
import com.rudhuvarshan.MovieBooking.model.Dto.MovieResponse;
import com.rudhuvarshan.MovieBooking.model.Dto.ShowResponse;
import com.rudhuvarshan.MovieBooking.model.Movie;
import com.rudhuvarshan.MovieBooking.model.Shows;
import com.rudhuvarshan.MovieBooking.repository.MovieDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MovieService {

    @Autowired
    private MovieDao movieDao;

    public List<Movie> addMovies(List<Movie> movies) {

        if (movies == null || movies.isEmpty()) {
            throw new BadRequestException(
                    "Movie list cannot be empty");
        }

        for (Movie movie : movies) {

            if (movie.getMovieName() == null ||
                    movie.getMovieName().isBlank()) {

                throw new BadRequestException(
                        "Movie name cannot be empty");
            }

            if (movie.getDuration() <= 0) {
                throw new BadRequestException(
                        "Movie duration must be greater than zero");
            }
        }

        return movieDao.saveAll(movies);
    }

    public List<MovieResponse> getAllMovies() {

        List<Movie> movies = movieDao.findAll();

        return movies.stream()
                .map(movie -> {

                    MovieResponse movieResponse =
                            new MovieResponse();

                    movieResponse.setMovieId(movie.getMovieId());
                    movieResponse.setMovieName(movie.getMovieName());
                    movieResponse.setCast(movie.getCast());
                    movieResponse.setDuration(movie.getDuration());
                    movieResponse.setLanguage(movie.getLanguage());

                    List<ShowResponse> showResponses =
                            movie.getShows() == null
                                    ? List.of()
                                    : movie.getShows()
                                    .stream()
                                    .map(show -> {

                                        ShowResponse showResponse =
                                                new ShowResponse();

                                        showResponse.setShowId(
                                                show.getShowId());

                                        showResponse.setShowDate(
                                                show.getShowDate());

                                        showResponse.setStartTime(
                                                show.getStartTime());

                                        if (show.getScreen() != null) {

                                            showResponse.setScreenName(
                                                    show.getScreen()
                                                            .getScreenName()
                                            );

                                            if (show.getScreen()
                                                    .getTheatre() != null) {

                                                showResponse.setTheatreName(
                                                        show.getScreen()
                                                                .getTheatre()
                                                                .getTheatreName()
                                                );
                                            }
                                        }

                                        return showResponse;

                                    })
                                    .toList();

                    movieResponse.setShows(showResponses);

                    return movieResponse;

                })
                .toList();
    }

    public Movie updateMovie(String movieName, Movie updatedMovie) {

        if (movieName == null || movieName.isBlank()) {
            throw new BadRequestException(
                    "Movie name cannot be empty");
        }

        if (updatedMovie == null) {
            throw new BadRequestException(
                    "Movie details cannot be null");
        }

        if (updatedMovie.getMovieName() == null ||
                updatedMovie.getMovieName().isBlank()) {

            throw new BadRequestException(
                    "Movie name cannot be empty");
        }

        if (updatedMovie.getDuration() <= 0) {
            throw new BadRequestException(
                    "Movie duration must be greater than zero");
        }

        Movie existingMovie = movieDao.findAll()
                .stream()
                .filter(movie ->
                        movie.getMovieName() != null &&
                                movie.getMovieName()
                                        .equalsIgnoreCase(movieName))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movie '" + movieName + "' not found"));

        existingMovie.setMovieName(
                updatedMovie.getMovieName());

        existingMovie.setCast(
                updatedMovie.getCast());

        existingMovie.setDuration(
                updatedMovie.getDuration());

        existingMovie.setLanguage(
                updatedMovie.getLanguage());

        return movieDao.save(existingMovie);
    }

    public String deleteMovie(String movieName) {

        if (movieName == null || movieName.isBlank()) {
            throw new BadRequestException(
                    "Movie name cannot be empty");
        }

        Movie movie = movieDao.findAll()
                .stream()
                .filter(existingMovie ->
                        existingMovie.getMovieName() != null &&
                                existingMovie.getMovieName()
                                        .equalsIgnoreCase(movieName))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movie '" + movieName + "' not found"));

        movieDao.delete(movie);

        return "Movie '" + movieName + "' deleted successfully";
    }
}