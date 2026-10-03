package com.rudhuvarshan.MovieBooking.repository;

import com.rudhuvarshan.MovieBooking.model.Screen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreenDao extends JpaRepository<Screen,Integer> {
    Screen findByScreenName(String screenName);
}
