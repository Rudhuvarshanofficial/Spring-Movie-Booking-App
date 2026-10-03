package com.rudhuvarshan.MovieBooking.repository;

import com.rudhuvarshan.MovieBooking.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersDao extends JpaRepository<Users,Integer> {
    Users findByEmail(String username);
}
