package com.rudhuvarshan.MovieBooking.controller;

import com.rudhuvarshan.MovieBooking.configuration.UserPrinciple;
import com.rudhuvarshan.MovieBooking.model.Dto.ShowResponse;
import com.rudhuvarshan.MovieBooking.model.ShowRequest;
import com.rudhuvarshan.MovieBooking.model.Shows;
import com.rudhuvarshan.MovieBooking.service.ShowsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("show")
public class ShowsController {

    @Autowired
    private ShowsService showsService;

    @PostMapping("")
    public List<Shows> addShows(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @RequestBody List<ShowRequest> showRequests) {

        return showsService.addShows(
                userPrinciple.getUsername(),
                showRequests
        );
    }

    @GetMapping("")
    public List<ShowResponse> getAllShows() {
        return showsService.getAllShows();
    }

    @PutMapping("update/{showId}")
    public Shows updateShow(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable int showId,
            @RequestBody ShowRequest updatedShow) {

        return showsService.updateShow(
                userPrinciple.getUsername(),
                showId,
                updatedShow
        );
    }

    @DeleteMapping("delete/{showId}")
    public String deleteShow(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable int showId) {

        return showsService.deleteShow(
                userPrinciple.getUsername(),
                showId
        );
    }
}