package com.Rothana.hotel_booking_system.features.HousekeepingTask;

import com.Rothana.hotel_booking_system.entity.HousekeepingStatus;
import com.Rothana.hotel_booking_system.entity.HousekeepingTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HousekeepingTaskRepository extends JpaRepository<HousekeepingTask, Integer> {

    List<HousekeepingTask> findAllByRoom_Id(Integer roomId);

    List<HousekeepingTask> findAllByAssignedTo_Id(Integer userId);

    List<HousekeepingTask> findAllByTaskDate(LocalDate taskDate);

    List<HousekeepingTask> findAllByStatus(HousekeepingStatus status);

    List<HousekeepingTask> findAllByRoom_IdAndTaskDate(Integer roomId, LocalDate taskDate);
}
