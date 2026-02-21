package com.Rothana.hotel_booking_system.features.Payments.dto;

public record PaypalCreateOrderResponse(
        String orderId,
        String approvalUrl
) {}
