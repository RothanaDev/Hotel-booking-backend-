package com.Rothana.hotel_booking_system.features.HousekeepingTask;

import com.Rothana.hotel_booking_system.entity.HousekeepingStatus;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskRequest;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskResponse;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskUpdateRequest;

import java.time.LocalDate;
import java.util.List;

public interface HousekeepingTaskService {

    HousekeepingTaskResponse create(HousekeepingTaskRequest request);

    HousekeepingTaskResponse findById(Integer id);

    List<HousekeepingTaskResponse> findAll();

    List<HousekeepingTaskResponse> findByRoom(Integer roomId);

    List<HousekeepingTaskResponse> findByAssignedTo(Integer userId);

    List<HousekeepingTaskResponse> findByDate(LocalDate date);

    List<HousekeepingTaskResponse> findByStatus(HousekeepingStatus status);

    HousekeepingTaskResponse update(Integer id, HousekeepingTaskUpdateRequest request);

    void delete(Integer id);
}
