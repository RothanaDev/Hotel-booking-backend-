package com.Rothana.hotel_booking_system.features.HousekeepingTask;

import com.Rothana.hotel_booking_system.entity.HousekeepingStatus;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskRequest;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskResponse;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/housekeeping-tasks")
@RequiredArgsConstructor
public class HousekeepingTaskController {

    private final HousekeepingTaskService service;

    @PostMapping
    public ResponseEntity<HousekeepingTaskResponse> create(@RequestBody HousekeepingTaskRequest request) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<HousekeepingTaskResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<HousekeepingTaskResponse>> findAll(
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) Integer assignedToId,
            @RequestParam(required = false) LocalDate date,
            @RequestParam(required = false) HousekeepingStatus status
    ) {
        if (roomId != null) return ResponseEntity.ok(service.findByRoom(roomId));
        if (assignedToId != null) return ResponseEntity.ok(service.findByAssignedTo(assignedToId));
        if (date != null) return ResponseEntity.ok(service.findByDate(date));
        if (status != null) return ResponseEntity.ok(service.findByStatus(status));
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<HousekeepingTaskResponse> update(
            @PathVariable Integer id,
            @RequestBody HousekeepingTaskUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
