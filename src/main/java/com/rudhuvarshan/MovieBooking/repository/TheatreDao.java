package com.rudhuvarshan.MovieBooking.repository;

import com.rudhuvarshan.MovieBooking.model.Theatre;
import com.rudhuvarshan.MovieBooking.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TheatreDao extends JpaRepository<Theatre,Integer> {
    Theatre findByManager(Users user);
}
