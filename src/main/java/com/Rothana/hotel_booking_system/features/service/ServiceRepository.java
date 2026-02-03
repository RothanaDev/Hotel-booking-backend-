package com.Rothana.hotel_booking_system.features.service;

import com.Rothana.hotel_booking_system.entity.HotelService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ServiceRepository extends JpaRepository<HotelService, Integer> {

    Optional<HotelService> findById(Integer id);
}
