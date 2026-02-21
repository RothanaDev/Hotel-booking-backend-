package com.Rothana.hotel_booking_system.features.MaintenanceTicket;

import com.Rothana.hotel_booking_system.entity.*;
import com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto.*;
import com.Rothana.hotel_booking_system.features.room.RoomRepository;
import com.Rothana.hotel_booking_system.features.user.UserRepository;
import com.Rothana.hotel_booking_system.mapper.MaintenanceTicketMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MaintenanceTicketServiceImpl implements MaintenanceTicketService {

    private final MaintenanceTicketRepository repository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final MaintenanceTicketMapper mapper;

    @Override
    public MaintenanceTicketResponse create(MaintenanceTicketRequest request) {

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.roomId()));

        User user = userRepository.findById(request.reportedById())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.reportedById()));

        // ✅ FEATURE: When ticket created, room becomes MAINTENANCE
        room.setStatus("MAINTENANCE");
        roomRepository.save(room);

        MaintenanceTicket ticket = mapper.toEntity(request);
        ticket.setRoom(room);
        ticket.setReportedBy(user);

        if (ticket.getPriority() == null) {
            ticket.setPriority(MaintenancePriority.LOW);
        }

        ticket.setStatus(MaintenanceStatus.OPEN);

        MaintenanceTicket saved = repository.save(ticket);
        return mapper.toResponse(saved);
    }

    @Override
    public MaintenanceTicketResponse findById(Integer id) {
        return mapper.toResponse(
                repository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Ticket not found: " + id))
        );
    }

    @Override
    public List<MaintenanceTicketResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<MaintenanceTicketResponse> findByRoom(Integer roomId) {
        return repository.findAllByRoom_Id(roomId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<MaintenanceTicketResponse> findByReportedBy(Integer userId) {
        return repository.findAllByReportedBy_Id(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<MaintenanceTicketResponse> findByStatus(MaintenanceStatus status) {
        return repository.findAllByStatus(status).stream().map(mapper::toResponse).toList();
    }

    @Override
    public MaintenanceTicketResponse update(Integer id, MaintenanceTicketUpdateRequest request) {

        MaintenanceTicket ticket = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));

        if (request.issue() != null) ticket.setIssue(request.issue());
        if (request.priority() != null) ticket.setPriority(request.priority());

        if (request.status() != null) {
            ticket.setStatus(request.status());

            Room room = ticket.getRoom();

            // ✅ FEATURE: if OPEN or FIXING -> Room stays MAINTENANCE
            if (request.status() == MaintenanceStatus.OPEN || request.status() == MaintenanceStatus.FIXING) {
                room.setStatus("MAINTENANCE");
                roomRepository.save(room);
            }

            // ✅ FEATURE: if RESOLVED -> set resolvedAt and release room
            if (request.status() == MaintenanceStatus.RESOLVED) {
                ticket.setResolvedAt(LocalDateTime.now());

                // Option A: room becomes AVAILABLE immediately
                room.setStatus("AVAILABLE");

                // Option B (more realistic): room becomes DIRTY, needs cleaning
                // room.setStatus("DIRTY");

                roomRepository.save(room);
            }
        }

        MaintenanceTicket saved = repository.save(ticket);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
