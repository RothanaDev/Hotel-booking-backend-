package com.Rothana.hotel_booking_system.features.RoomCalendar.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record RoomCalendarUpdateRequest(
        LocalDate date,          // optional: allow changing date (but must remain unique per room)
        Boolean isAvailable,
        BigDecimal priceOverride,
        String note
) {}
