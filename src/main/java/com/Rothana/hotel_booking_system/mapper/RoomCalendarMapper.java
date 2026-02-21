package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.RoomCalendar;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarRequest;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoomCalendarMapper {

    // room is set in service (because request only has roomId)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    RoomCalendar toEntity(RoomCalendarRequest request);

    // expose roomId from room.id
    @Mapping(target = "roomId", source = "room.id")
    RoomCalendarResponse toResponse(RoomCalendar entity);
}
