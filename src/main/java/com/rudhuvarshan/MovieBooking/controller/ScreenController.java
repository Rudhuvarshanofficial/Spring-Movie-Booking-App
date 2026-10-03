package com.rudhuvarshan.MovieBooking.controller;

import com.rudhuvarshan.MovieBooking.configuration.UserPrinciple;
import com.rudhuvarshan.MovieBooking.model.Screen;
import com.rudhuvarshan.MovieBooking.service.ScreenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("screen")
public class ScreenController {

    @Autowired
    private ScreenService screenService;

    @PostMapping("")
    public List<Screen> addScreen(
            @RequestBody List<Screen> screen,
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return screenService.addScreen(
                screen,
                userPrinciple.getUsername()
        );
    }

    @PostMapping("/add")
    public Screen addScreen(
            @RequestBody Screen screen,
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return screenService.addMoreScreen(
                screen,
                userPrinciple.getUsername()
        );
    }

    @GetMapping("")
    public List<Screen> allScreens(
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return screenService.addScreens(
                userPrinciple.getUsername()
        );
    }

    @PutMapping("/update/{screenName}")
    public Screen updateScreen(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable String screenName,
            @RequestBody Screen screen) {

        return screenService.update(
                userPrinciple.getUsername(),
                screenName,
                screen
        );
    }

    @DeleteMapping("delete/{screenName}")
    public String deleteScreen(
            @AuthenticationPrincipal UserPrinciple userPrinciple,
            @PathVariable String screenName) {

        return screenService.deleteScreen(
                userPrinciple.getUsername(),
                screenName
        );
    }
}