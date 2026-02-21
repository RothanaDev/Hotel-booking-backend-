package com.Rothana.hotel_booking_system.features.RoomCalendar;

import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarRequest;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarResponse;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarUpdateRequest;

import java.time.LocalDate;
import java.util.List;

public interface RoomCalendarService {

    RoomCalendarResponse create(RoomCalendarRequest request);

    RoomCalendarResponse findById(Integer id);

    RoomCalendarResponse findByRoomAndDate(Integer roomId, LocalDate date);

    List<RoomCalendarResponse> findByRoom(Integer roomId);

    List<RoomCalendarResponse> findByRoomBetweenDates(Integer roomId, LocalDate start, LocalDate end);

    RoomCalendarResponse update(Integer id, RoomCalendarUpdateRequest request);
    List<RoomCalendarResponse> findAll();


    void delete(Integer id);
}
