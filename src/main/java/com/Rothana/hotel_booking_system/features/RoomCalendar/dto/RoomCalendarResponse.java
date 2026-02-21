package com.Rothana.hotel_booking_system.features.RoomCalendar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record RoomCalendarResponse(
        Integer id,
        Integer roomId,
        LocalDate date,
        Boolean isAvailable,
        BigDecimal priceOverride,
        String note,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
