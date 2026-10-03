package com.rudhuvarshan.MovieBooking.repository;

import com.rudhuvarshan.MovieBooking.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieDao extends JpaRepository<Movie,Integer> {
}
