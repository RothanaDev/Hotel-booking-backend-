package com.Rothana.hotel_booking_system.features.RoomCalendar;

import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarRequest;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarResponse;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/room-calendar")
@RequiredArgsConstructor
public class RoomCalendarController {

    private final RoomCalendarService roomCalendarService;

    @PostMapping
    public ResponseEntity<RoomCalendarResponse> create(
            @RequestBody RoomCalendarRequest request
    ) {
        RoomCalendarResponse response = roomCalendarService.create(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoomCalendarResponse> findById(
            @PathVariable Integer id
    ) {
        RoomCalendarResponse response = roomCalendarService.findById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<RoomCalendarResponse>> findAll() {

        List<RoomCalendarResponse> responses =
                roomCalendarService.findAll();

        return ResponseEntity.ok(responses);
    }

    // =====================================
    // 3) GET Room Calendar by Room + Date
    // Example:
    // /api/room-calendar/room/5/date/2026-07-10
    // =====================================
    @GetMapping("/room/{roomId}/date/{date}")
    public ResponseEntity<RoomCalendarResponse> findByRoomAndDate(
            @PathVariable Integer roomId,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        RoomCalendarResponse response =
                roomCalendarService.findByRoomAndDate(roomId, date);

        return ResponseEntity.ok(response);
    }

    // =====================================
    // 4) GET All Calendar Records for Room
    // Example:
    // /api/room-calendar/room/5
    // =====================================
    @GetMapping("/room/{roomId}")
    public ResponseEntity<List<RoomCalendarResponse>> findByRoom(
            @PathVariable Integer roomId
    ) {
        List<RoomCalendarResponse> responses =
                roomCalendarService.findByRoom(roomId);

        return ResponseEntity.ok(responses);
    }

    // =====================================
    // 5) GET Calendar Records Between Dates
    // Example:
    // /api/room-calendar/room/5/range?start=2026-07-01&end=2026-07-10
    // =====================================
    @GetMapping("/room/{roomId}/range")
    public ResponseEntity<List<RoomCalendarResponse>> findByRoomBetweenDates(
            @PathVariable Integer roomId,
            @RequestParam LocalDate start,
            @RequestParam LocalDate end
    ) {
        List<RoomCalendarResponse> responses =
                roomCalendarService.findByRoomBetweenDates(roomId, start, end);

        return ResponseEntity.ok(responses);
    }

    // =====================================
    // 6) UPDATE Room Calendar Record
    // =====================================
    @PutMapping("/{id}")
    public ResponseEntity<RoomCalendarResponse> update(
            @PathVariable Integer id,
            @RequestBody RoomCalendarUpdateRequest request
    ) {
        RoomCalendarResponse response =
                roomCalendarService.update(id, request);

        return ResponseEntity.ok(response);
    }

    // =====================================
    // 7) DELETE Room Calendar Record
    // =====================================
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Integer id
    ) {
        roomCalendarService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
