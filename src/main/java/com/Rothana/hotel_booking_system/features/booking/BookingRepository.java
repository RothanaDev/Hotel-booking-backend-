package com.Rothana.hotel_booking_system.features.booking;

import com.Rothana.hotel_booking_system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Integer> {

    Optional<Booking> findById(Integer integer);
}
