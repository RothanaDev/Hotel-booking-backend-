package com.Rothana.hotel_booking_system.features.RoomCalendar;

import com.Rothana.hotel_booking_system.entity.Room;
import com.Rothana.hotel_booking_system.entity.RoomCalendar;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarRequest;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarResponse;
import com.Rothana.hotel_booking_system.features.RoomCalendar.dto.RoomCalendarUpdateRequest;
import com.Rothana.hotel_booking_system.features.room.RoomRepository;
import com.Rothana.hotel_booking_system.mapper.RoomCalendarMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RoomCalendarServiceImpl implements RoomCalendarService {

    private final RoomCalendarRepository roomCalendarRepository;
    private final RoomRepository roomRepository; // you likely already have this
    private final RoomCalendarMapper mapper;

    @Override
    public RoomCalendarResponse create(RoomCalendarRequest request) {
        if (request.roomId() == null) throw new IllegalArgumentException("roomId is required");
        if (request.date() == null) throw new IllegalArgumentException("date is required");

        // ensure room exists
        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.roomId()));

        // unique per room per date
        if (roomCalendarRepository.existsByRoom_IdAndDate(room.getId(), request.date())) {
            throw new RuntimeException("Room calendar already exists for roomId=" + room.getId() + " and date=" + request.date());
        }

        RoomCalendar entity = mapper.toEntity(request);
        entity.setRoom(room);

        // defaults if null
        if (entity.getIsAvailable() == null) entity.setIsAvailable(true);

        RoomCalendar saved = roomCalendarRepository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public RoomCalendarResponse findById(Integer id) {
        RoomCalendar rc = roomCalendarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomCalendar not found: " + id));
        return mapper.toResponse(rc);
    }

    @Override
    public RoomCalendarResponse findByRoomAndDate(Integer roomId, LocalDate date) {
        RoomCalendar rc = roomCalendarRepository.findByRoom_IdAndDate(roomId, date)
                .orElseThrow(() -> new RuntimeException("RoomCalendar not found for roomId=" + roomId + " date=" + date));
        return mapper.toResponse(rc);
    }

    @Override
    public List<RoomCalendarResponse> findByRoom(Integer roomId) {
        return roomCalendarRepository.findAllByRoom_Id(roomId)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public List<RoomCalendarResponse> findByRoomBetweenDates(Integer roomId, LocalDate start, LocalDate end) {
        return roomCalendarRepository.findAllByRoom_IdAndDateBetween(roomId, start, end)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }

    @Override
    public RoomCalendarResponse update(Integer id, RoomCalendarUpdateRequest request) {
        RoomCalendar rc = roomCalendarRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("RoomCalendar not found: " + id));

        // If changing date, respect unique constraint (room_id + date)
        if (request.date() != null && !request.date().equals(rc.getDate())) {
            Integer roomId = rc.getRoom().getId();
            if (roomCalendarRepository.existsByRoom_IdAndDate(roomId, request.date())) {
                throw new RuntimeException("Room calendar already exists for roomId=" + roomId + " and date=" + request.date());
            }
            rc.setDate(request.date());
        }

        if (request.isAvailable() != null) rc.setIsAvailable(request.isAvailable());
        if (request.priceOverride() != null) rc.setPriceOverride(request.priceOverride());
        if (request.note() != null) rc.setNote(request.note());

        RoomCalendar saved = roomCalendarRepository.save(rc);
        return mapper.toResponse(saved);
    }

    @Override
    public List<RoomCalendarResponse> findAll() {
        return roomCalendarRepository.findAll()
                .stream()
                .map(mapper::toResponse)
                .toList();
    }


    @Override
    public void delete(Integer id) {
        if (!roomCalendarRepository.existsById(id)) {
            throw new RuntimeException("RoomCalendar not found: " + id);
        }
        roomCalendarRepository.deleteById(id);
    }
}
