package com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto;

import com.Rothana.hotel_booking_system.entity.MaintenancePriority;

public record MaintenanceTicketRequest(
        Integer roomId,
        Integer reportedById,
        String issue,
        MaintenancePriority priority
) {}
