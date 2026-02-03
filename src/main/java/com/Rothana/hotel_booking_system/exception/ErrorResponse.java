package com.Rothana.hotel_booking_system.exception;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse<T> {
    private Integer code;
    T reason;
}