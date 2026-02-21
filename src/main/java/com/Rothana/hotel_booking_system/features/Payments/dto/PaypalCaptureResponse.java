package com.Rothana.hotel_booking_system.features.Payments.dto;


public record PaypalCaptureResponse(
        String orderId,
        String captureId,
        String status
) {}
