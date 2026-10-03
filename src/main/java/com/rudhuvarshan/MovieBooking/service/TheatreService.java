package com.rudhuvarshan.MovieBooking.service;

import com.rudhuvarshan.MovieBooking.exception.BadRequestException;
import com.rudhuvarshan.MovieBooking.exception.ForbiddenException;
import com.rudhuvarshan.MovieBooking.exception.ResourceNotFoundException;
import com.rudhuvarshan.MovieBooking.model.Theatre;
import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.repository.TheatreDao;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TheatreService {

    @Autowired
    private TheatreDao theatreDao;

    @Autowired
    private UsersDao usersDao;

    public Theatre addTheatre(
            Theatre theatre,
            String username) {

        if (theatre == null) {
            throw new BadRequestException(
                    "Theatre details cannot be null");
        }

        if (theatre.getTheatreName() == null ||
                theatre.getTheatreName().isBlank()) {

            throw new BadRequestException(
                    "Theatre name cannot be empty");
        }

        if (theatre.getLocation() == null ||
                theatre.getLocation().isBlank()) {

            throw new BadRequestException(
                    "Theatre location cannot be empty");
        }

        Users user = usersDao.findByEmail(username);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found");
        }

        Theatre existingTheatre =
                theatreDao.findByManager(user);

        if (existingTheatre != null) {
            throw new BadRequestException(
                    "You already have a theatre assigned");
        }

        user.setRole("MANAGER");

        theatre.setManager(user);

        usersDao.save(user);

        return theatreDao.save(theatre);
    }

    public Theatre profile(String username) {

        Users user = usersDao.findByEmail(username);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found");
        }

        Theatre theatre = theatreDao.findByManager(user);

        if (theatre == null) {
            throw new ResourceNotFoundException(
                    "No theatre found for this manager");
        }

        return theatre;
    }

    public List<Theatre> allTheatres() {

        return theatreDao.findAll();
    }

    public Theatre update(
            String username,
            Theatre updatedTheatre) {

        if (updatedTheatre == null) {
            throw new BadRequestException(
                    "Theatre details cannot be null");
        }

        if (updatedTheatre.getTheatreName() == null ||
                updatedTheatre.getTheatreName().isBlank()) {

            throw new BadRequestException(
                    "Theatre name cannot be empty");
        }

        if (updatedTheatre.getLocation() == null ||
                updatedTheatre.getLocation().isBlank()) {

            throw new BadRequestException(
                    "Theatre location cannot be empty");
        }

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

        theatre.setTheatreName(
                updatedTheatre.getTheatreName());

        theatre.setLocation(
                updatedTheatre.getLocation());

        return theatreDao.save(theatre);
    }
}