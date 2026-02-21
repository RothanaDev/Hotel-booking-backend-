package com.Rothana.hotel_booking_system.features.RoomCalendar;

import com.Rothana.hotel_booking_system.entity.RoomCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomCalendarRepository extends JpaRepository<RoomCalendar, Integer> {

    Optional<RoomCalendar> findByRoom_IdAndDate(Integer roomId, LocalDate date);

    boolean existsByRoom_IdAndDate(Integer roomId, LocalDate date);

    List<RoomCalendar> findAllByRoom_IdAndDateBetween(Integer roomId, LocalDate start, LocalDate end);

    List<RoomCalendar> findAllByRoom_Id(Integer roomId);
}
