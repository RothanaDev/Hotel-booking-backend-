package com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto;

import com.Rothana.hotel_booking_system.entity.MaintenancePriority;
import com.Rothana.hotel_booking_system.entity.MaintenanceStatus;

import java.time.LocalDateTime;

public record MaintenanceTicketUpdateRequest(
        String issue,
        MaintenancePriority priority,
        MaintenanceStatus status,
        LocalDateTime resolvedAt
) {}
