package com.rudhuvarshan.MovieBooking.repository;

import com.rudhuvarshan.MovieBooking.model.BookingSeat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingSeatDao extends JpaRepository<BookingSeat, Integer> {

    @Query("""
        SELECT bs.seat.seatId
        FROM BookingSeat bs
        WHERE bs.booking.show.showId = :showId
        AND bs.booking.status = 'CONFIRMED'
    """)
    List<Integer> findBookedSeatIds(@Param("showId") int showId);
}