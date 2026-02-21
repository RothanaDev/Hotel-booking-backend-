package com.Rothana.hotel_booking_system.features.booking.dto;

import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingResponse;
import com.Rothana.hotel_booking_system.features.auth.dto.UserResponse;
import com.Rothana.hotel_booking_system.features.room.dto.RoomResponse;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record BookingResponse(
        Integer id,
        UserResponse userResponse,
        RoomResponse roomResponse,
        LocalDate checkin,
        LocalDate checkout,
        int numOfAdults,
        int numOfChildren,
        int totalNumOfGuest,
        BigDecimal amount,
        String status,
        LocalDateTime createdAt,

        // ✅ NEW
        List<ServiceBookingResponse> bookingServices
) {}

