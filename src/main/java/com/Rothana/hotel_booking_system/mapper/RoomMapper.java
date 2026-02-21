package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.Room;
import com.Rothana.hotel_booking_system.features.room.dto.RoomCreateRequest;
import com.Rothana.hotel_booking_system.features.room.dto.RoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomMapper {

    RoomResponse toRoomResponse(Room room);

    List<RoomResponse> toRoomResponseList(List<Room> rooms);

    Room fromRoomCreateRequest(RoomCreateRequest roomCreateRequest);

    void updateRoomFromRequest(RoomCreateRequest roomCreateRequest, @MappingTarget Room room);

}
