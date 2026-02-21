package com.Rothana.hotel_booking_system.features.HousekeepingTask.dto;

import com.Rothana.hotel_booking_system.entity.HousekeepingStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record HousekeepingTaskResponse(
        Integer id,
        Integer roomId,
        Integer assignedToId,
        String assignedToName,
        LocalDate taskDate,
        HousekeepingStatus status,
        String remarks,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
