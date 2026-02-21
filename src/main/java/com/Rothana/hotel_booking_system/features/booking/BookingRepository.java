package com.Rothana.hotel_booking_system.features.booking;

import com.Rothana.hotel_booking_system.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking,Integer> {

    Optional<Booking> findById(Integer integer);
     @Query("""
        select b from Booking b
        left join fetch b.bookingServices bs
        left join fetch bs.service
        where b.id = :id
        """)
            Optional<Booking> findByIdWithServices(Integer id);
    List<Booking> findAllByUserId(Integer userId);

}
