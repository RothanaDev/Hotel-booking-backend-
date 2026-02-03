package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.BookingService;
import com.Rothana.hotel_booking_system.entity.Inventory;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryCreateRequest;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryResponse;
import com.Rothana.hotel_booking_system.features.Inventory.dto.InventoryUpdateRequest;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingCreateRequest;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingResponse;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InventoryMapper {

    InventoryResponse toInventoryResponse(Inventory inventory);
    Inventory fromInventoryCreateRequest(InventoryCreateRequest  request);
    List<InventoryResponse> toInventoryResponseList(List<Inventory> inventories);

    void updateInventoryFromRequest(InventoryUpdateRequest inventoryUpdateRequest, @MappingTarget Inventory inventory);
}
