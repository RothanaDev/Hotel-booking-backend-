package com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto;

import com.Rothana.hotel_booking_system.entity.MaintenancePriority;
import com.Rothana.hotel_booking_system.entity.MaintenanceStatus;

import java.time.LocalDateTime;

public record MaintenanceTicketResponse(
        Integer id,
        Integer roomId,
        Integer reportedById,
        String reportedByName,
        String issue,
        MaintenancePriority priority,
        MaintenanceStatus status,
        LocalDateTime openedAt,
        LocalDateTime resolvedAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
