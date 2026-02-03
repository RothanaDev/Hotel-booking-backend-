package com.Rothana.hotel_booking_system.features.RoomType.dto;

import java.math.BigDecimal;

public record RoomTypeResponse(
        Integer id,
        String typeName,
        String description,
        BigDecimal price
) {
}
