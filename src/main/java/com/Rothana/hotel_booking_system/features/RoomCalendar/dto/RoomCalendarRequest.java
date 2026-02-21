package com.Rothana.hotel_booking_system.features.RoomCalendar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomCalendarRequest(
        Integer roomId,
        LocalDate date,
        Boolean isAvailable,
        BigDecimal priceOverride,
        String note
) {}
