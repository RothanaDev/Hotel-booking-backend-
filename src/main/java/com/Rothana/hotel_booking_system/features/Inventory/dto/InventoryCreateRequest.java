package com.Rothana.hotel_booking_system.features.Inventory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;

@Builder
public record InventoryCreateRequest(
        @NotBlank
        String itemName,

        @NotNull
        @PositiveOrZero
        Integer quantity,

        String unit
) {
}
