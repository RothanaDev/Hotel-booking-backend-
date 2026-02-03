package com.Rothana.hotel_booking_system.features.ServiceBooking.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ServiceBookingCreateRequest(

        @NotNull
        Integer bookingId,

        @NotNull
        Integer serviceId,

        @Positive
        Integer quantity
) {
}
