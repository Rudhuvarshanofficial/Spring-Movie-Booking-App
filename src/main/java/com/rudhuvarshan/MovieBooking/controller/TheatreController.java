package com.rudhuvarshan.MovieBooking.controller;

import com.rudhuvarshan.MovieBooking.configuration.UserPrinciple;
import com.rudhuvarshan.MovieBooking.model.Theatre;
import com.rudhuvarshan.MovieBooking.service.TheatreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/theatre")
public class TheatreController {

    @Autowired
    private TheatreService theatreService;

    @GetMapping("")
    public List<Theatre> allTheatres() {

        return theatreService.allTheatres();
    }

    @PostMapping("/add")
    public Theatre addTheatre(
            @RequestBody Theatre theatre,
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return theatreService.addTheatre(
                theatre,
                userPrinciple.getUsername()
        );
    }

    @GetMapping("/profile")
    public Theatre profile(
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return theatreService.profile(
                userPrinciple.getUsername()
        );
    }

    @PutMapping("/update")
    public Theatre update(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @RequestBody Theatre updatedTheatre) {

        return theatreService.update(
                userPrinciple.getUsername(),
                updatedTheatre
        );
    }
}