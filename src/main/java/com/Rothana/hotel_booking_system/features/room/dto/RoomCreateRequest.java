package com.Rothana.hotel_booking_system.features.room.dto;

import jakarta.validation.constraints.NotNull;

public record RoomCreateRequest(
                @NotNull(message = "Room type is required") Integer roomTypeId,
                String status,
                String image) {
}
