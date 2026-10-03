package com.rudhuvarshan.MovieBooking.service;

import com.rudhuvarshan.MovieBooking.exception.BadRequestException;
import com.rudhuvarshan.MovieBooking.exception.ForbiddenException;
import com.rudhuvarshan.MovieBooking.exception.ResourceNotFoundException;
import com.rudhuvarshan.MovieBooking.model.Screen;
import com.rudhuvarshan.MovieBooking.model.Seat;
import com.rudhuvarshan.MovieBooking.model.Theatre;
import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.repository.SeatDao;
import com.rudhuvarshan.MovieBooking.repository.TheatreDao;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    @Autowired
    private SeatDao seatDao;

    @Autowired
    private TheatreDao theatreDao;

    @Autowired
    private UsersDao usersDao;

    public List<Seat> addSeats(
            String username,
            String screenName,
            List<Seat> seats) {

        if (screenName == null || screenName.isBlank()) {
            throw new BadRequestException(
                    "Screen name cannot be empty");
        }

        if (seats == null || seats.isEmpty()) {
            throw new BadRequestException(
                    "Seat list cannot be empty");
        }

        Theatre theatre = getManagerTheatre(username);

        Screen screen = findScreen(theatre, screenName);

        for (Seat seat : seats) {

            if (seat == null) {
                throw new BadRequestException(
                        "Seat cannot be null");
            }

            if (seat.getSeatRow() == null ||
                    seat.getSeatRow().isBlank()) {

                throw new BadRequestException(
                        "Seat row cannot be empty");
            }

            if (seat.getSeatNum() <= 0) {
                throw new BadRequestException(
                        "Seat number must be greater than zero");
            }

            if (seat.getSeatPrice() < 0) {
                throw new BadRequestException(
                        "Seat price cannot be negative");
            }

            if (seat.getType() == null ||
                    seat.getType().isBlank()) {

                throw new BadRequestException(
                        "Seat type cannot be empty");
            }

            seat.setScreen(screen);
        }

        return seatDao.saveAll(seats);
    }

    public List<Seat> getAllSeats(
            String username,
            String screenName) {

        if (screenName == null || screenName.isBlank()) {
            throw new BadRequestException(
                    "Screen name cannot be empty");
        }

        Theatre theatre = getManagerTheatre(username);

        Screen screen = findScreen(theatre, screenName);

        if (screen.getSeats() == null) {
            return List.of();
        }

        return screen.getSeats();
    }

    public Seat updateSeat(
            String username,
            String screenName,
            int seatId,
            Seat updatedSeat) {

        if (screenName == null || screenName.isBlank()) {
            throw new BadRequestException(
                    "Screen name cannot be empty");
        }

        if (seatId <= 0) {
            throw new BadRequestException(
                    "Invalid seat ID");
        }

        if (updatedSeat == null) {
            throw new BadRequestException(
                    "Seat details cannot be null");
        }

        Theatre theatre = getManagerTheatre(username);

        Screen screen = findScreen(theatre, screenName);

        Seat existingSeat = seatDao.findById(seatId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seat with ID " + seatId
                                        + " not found"));

        if (existingSeat.getScreen() == null ||
                existingSeat.getScreen().getScreenId()
                        != screen.getScreenId()) {

            throw new ForbiddenException(
                    "Seat " + seatId
                            + " does not belong to screen '"
                            + screenName + "'");
        }

        if (updatedSeat.getSeatRow() == null ||
                updatedSeat.getSeatRow().isBlank()) {

            throw new BadRequestException(
                    "Seat row cannot be empty");
        }

        if (updatedSeat.getSeatNum() <= 0) {
            throw new BadRequestException(
                    "Seat number must be greater than zero");
        }

        if (updatedSeat.getSeatPrice() < 0) {
            throw new BadRequestException(
                    "Seat price cannot be negative");
        }

        if (updatedSeat.getType() == null ||
                updatedSeat.getType().isBlank()) {

            throw new BadRequestException(
                    "Seat type cannot be empty");
        }

        existingSeat.setSeatRow(updatedSeat.getSeatRow());
        existingSeat.setSeatNum(updatedSeat.getSeatNum());
        existingSeat.setSeatPrice(updatedSeat.getSeatPrice());
        existingSeat.setType(updatedSeat.getType());

        return seatDao.save(existingSeat);
    }

    public String deleteSeat(
            String username,
            int seatId) {

        if (seatId <= 0) {
            throw new BadRequestException(
                    "Invalid seat ID");
        }

        Theatre theatre = getManagerTheatre(username);

        Seat seat = seatDao.findById(seatId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Seat with ID " + seatId
                                        + " not found"));

        if (seat.getScreen() == null ||
                seat.getScreen().getTheatre() == null) {

            throw new ResourceNotFoundException(
                    "Seat is not associated with a theatre");
        }

        if (seat.getScreen().getTheatre().getTheatreId()
                != theatre.getTheatreId()) {

            throw new ForbiddenException(
                    "You do not have permission to delete this seat");
        }

        seatDao.delete(seat);

        return "Seat " + seatId
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

    private Screen findScreen(
            Theatre theatre,
            String screenName) {

        if (theatre.getScreens() == null) {
            throw new ResourceNotFoundException(
                    "No screens found in your theatre");
        }

        return theatre.getScreens()
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
    }
}