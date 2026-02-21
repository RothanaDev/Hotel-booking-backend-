package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.HousekeepingTask;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskRequest;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface HousekeepingTaskMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)       // set in service using roomId
    @Mapping(target = "assignedTo", ignore = true) // set in service using assignedToId
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    HousekeepingTask toEntity(HousekeepingTaskRequest request);

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "assignedToId", source = "assignedTo.id")
    @Mapping(target = "assignedToName", source = "assignedTo.name")
    HousekeepingTaskResponse toResponse(HousekeepingTask entity);
}
