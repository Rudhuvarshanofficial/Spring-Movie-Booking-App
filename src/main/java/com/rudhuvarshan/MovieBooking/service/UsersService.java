package com.rudhuvarshan.MovieBooking.service;

import com.rudhuvarshan.MovieBooking.exception.BadRequestException;
import com.rudhuvarshan.MovieBooking.exception.ResourceNotFoundException;
import com.rudhuvarshan.MovieBooking.model.Users;
import com.rudhuvarshan.MovieBooking.repository.UsersDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersDao usersDao;

    private final BCryptPasswordEncoder encoder =
            new BCryptPasswordEncoder(12);

    public Users register(Users user) {

        if (user == null) {
            throw new BadRequestException(
                    "User details cannot be null");
        }

        if (user.getFullName() == null ||
                user.getFullName().isBlank()) {

            throw new BadRequestException(
                    "Full name cannot be empty");
        }

        if (user.getEmail() == null ||
                user.getEmail().isBlank()) {

            throw new BadRequestException(
                    "Email cannot be empty");
        }

        if (user.getPassword() == null ||
                user.getPassword().isBlank()) {

            throw new BadRequestException(
                    "Password cannot be empty");
        }

        if (usersDao.findByEmail(user.getEmail()) != null) {
            throw new BadRequestException(
                    "An account with this email already exists");
        }

        user.setPassword(
                encoder.encode(user.getPassword()));

        // Role must never come from the client.
        user.setRole("CUSTOMER");

        return usersDao.save(user);
    }

    public Users profile(String username) {

        Users user = usersDao.findByEmail(username);

        if (user == null) {
            throw new ResourceNotFoundException(
                    "User not found");
        }

        return user;
    }

    public Users update(
            Users updatedUser,
            String username) {

        if (updatedUser == null) {
            throw new BadRequestException(
                    "User details cannot be null");
        }

        Users existingUser = usersDao.findByEmail(username);

        if (existingUser == null) {
            throw new ResourceNotFoundException(
                    "User not found");
        }

        if (updatedUser.getFullName() != null &&
                !updatedUser.getFullName().isBlank()) {

            existingUser.setFullName(
                    updatedUser.getFullName());
        }

        if (updatedUser.getPhoneNumber() != null) {
            existingUser.setPhoneNumber(
                    updatedUser.getPhoneNumber());
        }

        if (updatedUser.getLocation() != null) {
            existingUser.setLocation(
                    updatedUser.getLocation());
        }

        if (updatedUser.getPassword() != null &&
                !updatedUser.getPassword().isBlank()) {

            existingUser.setPassword(
                    encoder.encode(
                            updatedUser.getPassword()));
        }

        return usersDao.save(existingUser);
    }
}