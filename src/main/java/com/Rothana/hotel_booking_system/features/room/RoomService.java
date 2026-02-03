package com.Rothana.hotel_booking_system.features.room;

import com.Rothana.hotel_booking_system.features.room.dto.RoomCreateRequest;
import com.Rothana.hotel_booking_system.features.room.dto.RoomResponse;
import com.Rothana.hotel_booking_system.features.room.dto.RoomUpdate;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface RoomService {
    RoomResponse create(Integer roomTypeId, String status, MultipartFile image);

    RoomResponse update(Integer id, Integer roomTypeId, String status, MultipartFile image);


    RoomResponse findById(Integer id);

    List<RoomResponse> findAll();

    void delete(Integer id);
}
