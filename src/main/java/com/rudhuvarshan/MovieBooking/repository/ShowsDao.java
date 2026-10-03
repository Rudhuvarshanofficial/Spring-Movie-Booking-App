package com.rudhuvarshan.MovieBooking.repository;

import com.rudhuvarshan.MovieBooking.model.Shows;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShowsDao extends JpaRepository<Shows, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT s FROM Shows s WHERE s.showId = :showId")
    Optional<Shows> findByIdForUpdate(@Param("showId") int showId);
}