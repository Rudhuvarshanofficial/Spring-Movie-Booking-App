package com.rudhuvarshan.MovieBooking.service;

import com.rudhuvarshan.MovieBooking.exception.BadRequestException;
import com.rudhuvarshan.MovieBooking.exception.ForbiddenException;
import com.rudhuvarshan.MovieBooking.exception.ResourceNotFoundException;
import com.rudhuvarshan.MovieBooking.model.Screen;
import com.rudhuvarshan.MovieBooking.model.Theatre;
import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.repository.ScreenDao;
import com.rudhuvarshan.MovieBooking.repository.TheatreDao;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScreenService {

    @Autowired
    private ScreenDao screenDao;

    @Autowired
    private TheatreDao theatreDao;

    @Autowired
    private UsersDao usersDao;

    public List<Screen> addScreen(
            List<Screen> screens,
            String username) {

        if (screens == null || screens.isEmpty()) {
            throw new BadRequestException(
                    "Screen list cannot be empty");
        }

        Theatre theatre = getManagerTheatre(username);

        for (Screen screen : screens) {

            if (screen.getScreenName() == null ||
                    screen.getScreenName().isBlank()) {

                throw new BadRequestException(
                        "Screen name cannot be empty");
            }

            screen.setTheatre(theatre);
        }

        return screenDao.saveAll(screens);
    }

    public List<Screen> addScreens(String username) {

        Theatre theatre = getManagerTheatre(username);

        if (theatre.getScreens() == null) {
            return List.of();
        }

        return theatre.getScreens();
    }

    public Screen update(
            String username,
            String screenName,
            Screen updatedScreen) {

        if (screenName == null || screenName.isBlank()) {
            throw new BadRequestException(
                    "Screen name cannot be empty");
        }

        if (updatedScreen == null) {
            throw new BadRequestException(
                    "Screen details cannot be null");
        }

        if (updatedScreen.getScreenName() == null ||
                updatedScreen.getScreenName().isBlank()) {

            throw new BadRequestException(
                    "New screen name cannot be empty");
        }

        Theatre theatre = getManagerTheatre(username);

        Screen existingScreen = theatre.getScreens()
                .stream()
                .filter(screen ->
                        screen.getScreenName() != null &&
                                screen.getScreenName()
                                        .equalsIgnoreCase(screenName))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Screen '" + screenName
                                        + "' not found"));

        existingScreen.setScreenName(
                updatedScreen.getScreenName());

        return screenDao.save(existingScreen);
    }

    public String deleteScreen(
            String username,
            String screenName) {

        if (screenName == null || screenName.isBlank()) {
            throw new BadRequestException(
                    "Screen name cannot be empty");
        }

        Theatre theatre = getManagerTheatre(username);

        Screen screen = theatre.getScreens()
                .stream()
                .filter(existingScreen ->
                        existingScreen.getScreenName() != null &&
                                existingScreen.getScreenName()
                                        .equalsIgnoreCase(screenName))
                .findFirst()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Screen '" + screenName
                                        + "' not found"));

        if (screen.getSeats() != null &&
                !screen.getSeats().isEmpty()) {

            throw new BadRequestException(
                    "Cannot delete a screen that contains seats");
        }

        if (screen.getShows() != null &&
                !screen.getShows().isEmpty()) {

            throw new BadRequestException(
                    "Cannot delete a screen that has shows");
        }

        screenDao.delete(screen);

        return "Screen '" + screenName
                + "' deleted successfully";
    }

    public Screen addMoreScreen(
            Screen screen,
            String username) {

        if (screen == null) {
            throw new BadRequestException(
                    "Screen details cannot be null");
        }

        if (screen.getScreenName() == null ||
                screen.getScreenName().isBlank()) {

            throw new BadRequestException(
                    "Screen name cannot be empty");
        }

        Theatre theatre = getManagerTheatre(username);

        screen.setTheatre(theatre);

        return screenDao.save(screen);
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
}