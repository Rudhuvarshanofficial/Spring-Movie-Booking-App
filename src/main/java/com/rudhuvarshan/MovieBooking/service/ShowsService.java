package com.rudhuvarshan.MovieBooking.service;

import com.rudhuvarshan.MovieBooking.exception.BadRequestException;
import com.rudhuvarshan.MovieBooking.exception.ForbiddenException;
import com.rudhuvarshan.MovieBooking.exception.ResourceNotFoundException;
import com.rudhuvarshan.MovieBooking.model.Dto.ShowResponse;
import com.rudhuvarshan.MovieBooking.model.Movie;
import com.rudhuvarshan.MovieBooking.model.Screen;
import com.rudhuvarshan.MovieBooking.model.ShowRequest;
import com.rudhuvarshan.MovieBooking.model.Shows;
import com.rudhuvarshan.MovieBooking.model.Theatre;
import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.repository.MovieDao;
import com.rudhuvarshan.MovieBooking.repository.ScreenDao;
import com.rudhuvarshan.MovieBooking.repository.ShowsDao;
import com.rudhuvarshan.MovieBooking.repository.TheatreDao;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShowsService {

    @Autowired
    private ShowsDao showsDao;

    @Autowired
    private MovieDao movieDao;

    @Autowired
    private ScreenDao screenDao;

    @Autowired
    private TheatreDao theatreDao;

    @Autowired
    private UsersDao usersDao;

    public List<Shows> addShows(
            String username,
            List<ShowRequest> showRequests) {

        if (showRequests == null || showRequests.isEmpty()) {
            throw new BadRequestException(
                    "Show list cannot be empty");
        }

        Theatre theatre = getManagerTheatre(username);

        List<Shows> shows = new ArrayList<>();

        for (ShowRequest request : showRequests) {

            validateShowRequest(request);

            Movie movie = movieDao.findById(request.getMovieId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Movie with ID "
                                            + request.getMovieId()
                                            + " not found"));

            Screen screen = screenDao.findById(request.getScreenId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException(
                                    "Screen with ID "
                                            + request.getScreenId()
                                            + " not found"));

            if (screen.getTheatre() == null ||
                    screen.getTheatre().getTheatreId()
                            != theatre.getTheatreId()) {

                throw new ForbiddenException(
                        "You do not have permission to create a show "
                                + "for this screen");
            }

            Shows show = new Shows();

            show.setShowDate(request.getShowDate());
            show.setStartTime(request.getStartTime());
            show.setMovie(movie);
            show.setScreen(screen);

            shows.add(show);
        }

        return showsDao.saveAll(shows);
    }

    public List<ShowResponse> getAllShows() {

        List<Shows> shows = showsDao.findAll();

        return shows.stream()
                .map(show -> {

                    ShowResponse response =
                            new ShowResponse();

                    response.setShowId(show.getShowId());
                    response.setShowDate(show.getShowDate());
                    response.setStartTime(show.getStartTime());

                    if (show.getScreen() != null) {

                        response.setScreenName(
                                show.getScreen()
                                        .getScreenName());

                        if (show.getScreen().getTheatre() != null) {

                            response.setTheatreName(
                                    show.getScreen()
                                            .getTheatre()
                                            .getTheatreName());
                        }
                    }

                    return response;

                })
                .toList();
    }

    public Shows updateShow(
            String username,
            int showId,
            ShowRequest showRequest) {

        validateShowRequest(showRequest);

        Theatre theatre = getManagerTheatre(username);

        Shows show = showsDao.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show with ID " + showId
                                        + " not found"));

        verifyShowOwnership(show, theatre);

        Movie movie = movieDao.findById(
                        showRequest.getMovieId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Movie with ID "
                                        + showRequest.getMovieId()
                                        + " not found"));

        Screen screen = screenDao.findById(
                        showRequest.getScreenId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Screen with ID "
                                        + showRequest.getScreenId()
                                        + " not found"));

        if (screen.getTheatre() == null ||
                screen.getTheatre().getTheatreId()
                        != theatre.getTheatreId()) {

            throw new ForbiddenException(
                    "You do not have permission to move this show "
                            + "to the selected screen");
        }

        show.setShowDate(showRequest.getShowDate());
        show.setStartTime(showRequest.getStartTime());
        show.setMovie(movie);
        show.setScreen(screen);

        return showsDao.save(show);
    }

    public String deleteShow(
            String username,
            int showId) {

        Theatre theatre = getManagerTheatre(username);

        Shows show = showsDao.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show with ID " + showId
                                        + " not found"));

        verifyShowOwnership(show, theatre);

        showsDao.delete(show);

        return "Show " + showId
                + " deleted successfully";
    }

    private Theatre getManagerTheatre(String username) {

        Users user = usersDao.findByEmail(username);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found");
        }

        Theatre theatre = theatreDao.findByManager(user);

        if (theatre == null) {
            throw new ForbiddenException(
                    "You do not have a theatre assigned to manage");
        }

        return theatre;
    }

    private void verifyShowOwnership(
            Shows show,
            Theatre theatre) {

        if (show.getScreen() == null ||
                show.getScreen().getTheatre() == null) {

            throw new ResourceNotFoundException(
                    "Show is not associated with a theatre");
        }

        if (show.getScreen().getTheatre().getTheatreId()
                != theatre.getTheatreId()) {

            throw new ForbiddenException(
                    "You do not have permission to modify this show");
        }
    }

    private void validateShowRequest(
            ShowRequest request) {

        if (request == null) {
            throw new BadRequestException(
                    "Show request cannot be null");
        }

        if (request.getShowDate() == null) {
            throw new BadRequestException(
                    "Show date cannot be null");
        }

        if (request.getStartTime() == null) {
            throw new BadRequestException(
                    "Show start time cannot be null");
        }

        if (request.getMovieId() <= 0) {
            throw new BadRequestException(
                    "Invalid movie ID");
        }

        if (request.getScreenId() <= 0) {
            throw new BadRequestException(
                    "Invalid screen ID");
        }
    }
}