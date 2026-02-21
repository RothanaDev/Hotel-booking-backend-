package com.Rothana.hotel_booking_system.features.ServiceBooking.dto;

import com.Rothana.hotel_booking_system.features.service.dto.ServiceResponse;

import java.math.BigDecimal;

public record ServiceBookingResponse(
        Integer id,
        ServiceResponse serviceResponse,
        Integer quantity,
        BigDecimal totalAmount
) {}
