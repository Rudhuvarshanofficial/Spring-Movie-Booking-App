package com.rudhuvarshan.MovieBooking.controller;

import com.rudhuvarshan.MovieBooking.configuration.UserPrinciple;
import com.rudhuvarshan.MovieBooking.model.Dto.BookingRequest;
import com.rudhuvarshan.MovieBooking.model.Dto.BookingResponse;
import com.rudhuvarshan.MovieBooking.model.Seat;
import com.rudhuvarshan.MovieBooking.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @PostMapping("/seat/{showId}")
    public ResponseEntity<List<Seat>> getAvailableSeats(
            @PathVariable int showId) {

        return ResponseEntity.ok(
                bookingService.getAvailableSeats(showId)
        );
    }

    @PostMapping("")
    public ResponseEntity<BookingResponse> book(
            @RequestBody BookingRequest request,
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return ResponseEntity.ok(
                bookingService.book(
                        userPrinciple.getUsername(),
                        request
                )
        );
    }
}