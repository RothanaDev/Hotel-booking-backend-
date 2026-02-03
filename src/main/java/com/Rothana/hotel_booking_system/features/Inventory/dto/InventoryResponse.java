package com.Rothana.hotel_booking_system.features.Inventory.dto;

public record InventoryResponse(
        Integer id,
        String itemName,
        Integer quantity,
        String unit
) {
}
