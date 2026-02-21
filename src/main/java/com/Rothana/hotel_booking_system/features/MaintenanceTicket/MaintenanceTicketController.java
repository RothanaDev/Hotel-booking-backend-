package com.Rothana.hotel_booking_system.features.MaintenanceTicket;

import com.Rothana.hotel_booking_system.entity.MaintenanceStatus;
import com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/maintenance-tickets")
@RequiredArgsConstructor
public class MaintenanceTicketController {

    private final MaintenanceTicketService service;

    @PostMapping
    public ResponseEntity<MaintenanceTicketResponse> create(
            @RequestBody MaintenanceTicketRequest request
    ) {
        return ResponseEntity.ok(service.create(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintenanceTicketResponse> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<MaintenanceTicketResponse>> findAll(
            @RequestParam(required = false) Integer roomId,
            @RequestParam(required = false) Integer reportedById,
            @RequestParam(required = false) MaintenanceStatus status
    ) {
        if (roomId != null) return ResponseEntity.ok(service.findByRoom(roomId));
        if (reportedById != null) return ResponseEntity.ok(service.findByReportedBy(reportedById));
        if (status != null) return ResponseEntity.ok(service.findByStatus(status));
        return ResponseEntity.ok(service.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceTicketResponse> update(
            @PathVariable Integer id,
            @RequestBody MaintenanceTicketUpdateRequest request
    ) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
