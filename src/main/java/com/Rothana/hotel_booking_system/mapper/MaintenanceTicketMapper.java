package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.MaintenanceTicket;
import com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto.MaintenanceTicketRequest;
import com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto.MaintenanceTicketResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MaintenanceTicketMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "room", ignore = true)
    @Mapping(target = "reportedBy", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "openedAt", ignore = true)
    @Mapping(target = "resolvedAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    MaintenanceTicket toEntity(MaintenanceTicketRequest request);

    @Mapping(target = "roomId", source = "room.id")
    @Mapping(target = "reportedById", source = "reportedBy.id")
    @Mapping(target = "reportedByName", source = "reportedBy.name")
    MaintenanceTicketResponse toResponse(MaintenanceTicket entity);
}
