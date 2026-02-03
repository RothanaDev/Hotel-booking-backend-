package com.Rothana.hotel_booking_system.features.RoomType;

import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeCreateRequest;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeResponse;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeUpdateRequest;

import java.util.List;

public interface RoomTypeService {


    RoomTypeResponse create(RoomTypeCreateRequest request);
    RoomTypeResponse update(Integer id, RoomTypeUpdateRequest request);
    RoomTypeResponse findById(Integer id);
    List<RoomTypeResponse> findAll();
    void delete(Integer id);


}
