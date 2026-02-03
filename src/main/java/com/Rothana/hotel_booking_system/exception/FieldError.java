package com.Rothana.hotel_booking_system.exception;

import lombok.Builder;

@Builder
public record FieldError(
        String field,
        String detail
) {

}
