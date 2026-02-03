package com.Rothana.hotel_booking_system.features.Inventory.dto;

import jakarta.validation.constraints.PositiveOrZero;

public record InventoryUpdateRequest(
        String itemName,

        @PositiveOrZero
        Integer quantity,

        String unit
) {
}
