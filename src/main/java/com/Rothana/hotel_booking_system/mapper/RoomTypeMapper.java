package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.RoomType;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeCreateRequest;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeResponse;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoomTypeMapper {

    RoomTypeResponse toRoomTypeResponse(RoomType roomType);
    RoomType fromRoomTypeCreateRequest(RoomTypeCreateRequest roomTypeCreateRequest);
    List<RoomTypeResponse> toRoomTypeResponseList(List<RoomType> roomTypes);

    void updateRoomTypeFromRequest(RoomTypeUpdateRequest request, @MappingTarget RoomType roomType);

}
