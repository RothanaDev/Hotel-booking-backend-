package com.Rothana.hotel_booking_system.features.RoomType;

import com.Rothana.hotel_booking_system.entity.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoomTypeRepository extends JpaRepository<RoomType, Integer> {

    Optional<RoomType> findById(Integer id);
}

