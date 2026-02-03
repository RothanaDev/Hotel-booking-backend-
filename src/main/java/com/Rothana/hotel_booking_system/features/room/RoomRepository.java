package com.Rothana.hotel_booking_system.features.room;

import com.Rothana.hotel_booking_system.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Integer> {
}
