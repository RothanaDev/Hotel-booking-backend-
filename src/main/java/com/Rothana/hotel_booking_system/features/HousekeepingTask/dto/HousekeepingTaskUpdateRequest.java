package com.Rothana.hotel_booking_system.features.HousekeepingTask.dto;

import com.Rothana.hotel_booking_system.entity.HousekeepingStatus;

import java.time.LocalDate;

public record HousekeepingTaskUpdateRequest(
        Integer assignedToId,
        LocalDate taskDate,
        HousekeepingStatus status,
        String remarks
) {}
