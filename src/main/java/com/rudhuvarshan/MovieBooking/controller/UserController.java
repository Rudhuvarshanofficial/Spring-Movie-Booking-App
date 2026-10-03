package com.rudhuvarshan.MovieBooking.controller;

import com.rudhuvarshan.MovieBooking.configuration.UserPrinciple;
import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    private UsersService usersService;

    @GetMapping("/profile")
    public Users profile(
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return usersService.profile(
                userPrinciple.getUsername()
        );
    }

    @PostMapping("/register")
    public Users register(
            @RequestBody Users user) {

        return usersService.register(user);
    }

    @PutMapping("/update")
    public Users update(
            @RequestBody Users user,
            @AuthenticationPrincipal UserPrinciple userPrinciple) {

        return usersService.update(
                user,
                userPrinciple.getUsername()
        );
    }
}