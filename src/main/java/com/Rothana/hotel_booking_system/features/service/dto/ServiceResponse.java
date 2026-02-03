package com.Rothana.hotel_booking_system.features.service.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ServiceResponse(
        Integer id,
        String serviceName,
        String description,
        BigDecimal price,
        String category,
        String image
) {
}
