package com.Rothana.hotel_booking_system.features.Inventory;

import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryCreateRequest;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryResponse;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryUpdateRequest;

import java.util.List;

public interface InventoryService {

    InventoryResponse create(InventoryCreateRequest request);
    InventoryResponse update(Integer id, InventoryUpdateRequest request);
    InventoryResponse findById(Integer id);
    List<InventoryResponse> findAll();
    void delete(Integer id);
}
