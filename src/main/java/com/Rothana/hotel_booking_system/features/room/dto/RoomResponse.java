package com.Rothana.hotel_booking_system.features.room.dto;

import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeResponse;
import lombok.Builder;

@Builder
public record RoomResponse(
                Integer id,
                String status,
                String image,
                RoomTypeResponse roomType

) {
}
