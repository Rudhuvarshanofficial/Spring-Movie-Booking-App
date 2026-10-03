package com.rudhuvarshan.MovieBooking.controller;

import com.rudhuvarshan.MovieBooking.configuration.UserPrinciple;
import com.rudhuvarshan.MovieBooking.model.Seat;
import com.rudhuvarshan.MovieBooking.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("seat")
public class SeatController {

    @Autowired
    private SeatService seatService;

    @PostMapping("/{screenName}")
    public List<Seat> addSeats(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable String screenName,
            @RequestBody List<Seat> seats) {

        return seatService.addSeats(
                userPrinciple.getUsername(),
                screenName,
                seats
        );
    }

    @GetMapping("/{screenName}")
    public List<Seat> getAllSeats(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable String screenName) {

        return seatService.getAllSeats(
                userPrinciple.getUsername(),
                screenName
        );
    }

    @PutMapping("/{screenName}/{seatId}")
    public Seat updateSeat(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable String screenName,
            @PathVariable int seatId,
            @RequestBody Seat updatedSeat) {

        return seatService.updateSeat(
                userPrinciple.getUsername(),
                screenName,
                seatId,
                updatedSeat
        );
    }

    @DeleteMapping("/{seatId}")
    public String deleteSeat(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable int seatId) {

        return seatService.deleteSeat(
                userPrinciple.getUsername(),
                seatId
        );
    }
}