package com.rudhuvarshan.MovieBooking.service;

import com.rudhuvarshan.MovieBooking.exception.BadRequestException;
import com.rudhuvarshan.MovieBooking.exception.ResourceNotFoundException;
import com.rudhuvarshan.MovieBooking.exception.SeatAlreadyBookedException;
import com.rudhuvarshan.MovieBooking.model.BookingSeat;
import com.rudhuvarshan.MovieBooking.model.Bookings;
import com.rudhuvarshan.MovieBooking.model.Seat;
import com.rudhuvarshan.MovieBooking.model.Shows;
import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.model.Dto.BookingRequest;
import com.rudhuvarshan.MovieBooking.model.Dto.BookingResponse;
import com.rudhuvarshan.MovieBooking.repository.BookingDao;
import com.rudhuvarshan.MovieBooking.repository.BookingSeatDao;
import com.rudhuvarshan.MovieBooking.repository.SeatDao;
import com.rudhuvarshan.MovieBooking.repository.ShowsDao;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BookingService {

    @Autowired
    private BookingDao bookingDao;

    @Autowired
    private BookingSeatDao bookingSeatDao;

    @Autowired
    private ShowsDao showsDao;

    @Autowired
    private SeatDao seatDao;

    @Autowired
    private UsersDao usersDao;

    public List<Seat> getAvailableSeats(int showId) {

        Shows show = showsDao.findById(showId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Show with ID " + showId + " not found"));

        List<Seat> seats = show.getScreen().getSeats();

        List<Integer> bookedSeatIds =
                bookingSeatDao.findBookedSeatIds(showId);

        return seats.stream()
                .filter(seat -> !bookedSeatIds.contains(seat.getSeatId()))
                .toList();
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponse book(String username, BookingRequest request) {

        if (request == null) {
            throw new BadRequestException("Booking request cannot be null");
        }

        if (request.getSeatIds() == null || request.getSeatIds().isEmpty()) {
            throw new BadRequestException("At least one seat must be selected");
        }

        Users user = usersDao.findByEmail(username);

        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Shows show = showsDao.findByIdForUpdate(request.getShowId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Show with ID " + request.getShowId() + " not found"));

        Set<Integer> uniqueSeatIds =
                new HashSet<>(request.getSeatIds());

        if (uniqueSeatIds.size() != request.getSeatIds().size()) {
            throw new BadRequestException(
                    "Duplicate seat IDs are not allowed");
        }

        List<Seat> seats = seatDao.findAllById(request.getSeatIds());

        if (seats.size() != request.getSeatIds().size()) {
            throw new ResourceNotFoundException(
                    "One or more selected seats were not found");
        }

        for (Seat seat : seats) {

            if (seat.getScreen() == null ||
                    seat.getScreen().getScreenId()
                            != show.getScreen().getScreenId()) {

                throw new BadRequestException(
                        "Seat " + seat.getSeatId()
                                + " does not belong to this show's screen");
            }
        }

        List<Integer> bookedSeatIds =
                bookingSeatDao.findBookedSeatIds(request.getShowId());

        for (Seat seat : seats) {

            if (bookedSeatIds.contains(seat.getSeatId())) {

                throw new SeatAlreadyBookedException(
                        "Seat " + seat.getSeatId() + " is already booked");
            }
        }

        double totalAmount = seats.stream()
                .mapToDouble(Seat::getSeatPrice)
                .sum();

        Bookings booking = new Bookings();

        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setTotalAmount(totalAmount);
        booking.setStatus("CONFIRMED");

        booking = bookingDao.save(booking);

        List<BookingSeat> bookingSeats = new ArrayList<>();

        for (Seat seat : seats) {

            BookingSeat bookingSeat = new BookingSeat();

            bookingSeat.setBooking(booking);
            bookingSeat.setSeat(seat);

            bookingSeats.add(bookingSeat);
        }

        bookingSeatDao.saveAll(bookingSeats);

        booking.setBookingSeats(bookingSeats);

        BookingResponse response = new BookingResponse();

        response.setBookingId(booking.getBookingId());
        response.setMovieName(show.getMovie().getMovieName());
        response.setTheatreName(
                show.getScreen()
                        .getTheatre()
                        .getTheatreName()
        );
        response.setScreenName(
                show.getScreen().getScreenName()
        );
        response.setShowDate(show.getShowDate());
        response.setStartTime(show.getStartTime());

        response.setSeatIds(
                seats.stream()
                        .map(Seat::getSeatId)
                        .toList()
        );

        response.setTotalAmount(totalAmount);
        response.setBookingTime(booking.getBookingTime());
        response.setStatus(booking.getStatus());

        return response;
    }
}