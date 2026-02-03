package com.Rothana.hotel_booking_system.features.service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ServiceUpdateRequest(
        @NotBlank(message = "Service Name is Required")
        String serviceName,

        @NotBlank(message = "Description is Required")
        String description,

        @NotNull(message = "Price is Required")
        @DecimalMin(value = "0.01", message = "Price must be greater than 0")
        BigDecimal price
) {
}
