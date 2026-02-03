package com.Rothana.hotel_booking_system.features.ServiceBooking;

import com.Rothana.hotel_booking_system.entity.BookingService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceBookingRepository  extends JpaRepository<BookingService,Integer> {
    Optional<BookingService> findById(int id);
}
