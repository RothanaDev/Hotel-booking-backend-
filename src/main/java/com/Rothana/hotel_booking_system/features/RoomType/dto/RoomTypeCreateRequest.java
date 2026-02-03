package com.Rothana.hotel_booking_system.features.RoomType.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RoomTypeCreateRequest(

        @NotBlank(message = "Type Name is Required")
        String typeName,
        @NotBlank(message = "Description is required")
        String description,
        @NotNull(message = "Price is required")
        @Positive(message = "Price must be greater than 0")
        BigDecimal price

) {
}
