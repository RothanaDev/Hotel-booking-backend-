package com.Rothana.hotel_booking_system.features.ServiceBooking.dto;

import com.Rothana.hotel_booking_system.features.booking.dto.BookingResponse;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceResponse;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ServiceBookingResponse(

        BookingResponse bookingResponse,
        ServiceResponse serviceResponse,
        BigDecimal totalAmount
) {
}
