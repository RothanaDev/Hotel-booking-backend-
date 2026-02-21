package com.Rothana.hotel_booking_system.features.HousekeepingTask;

import com.Rothana.hotel_booking_system.entity.HousekeepingStatus;
import com.Rothana.hotel_booking_system.entity.HousekeepingTask;
import com.Rothana.hotel_booking_system.entity.Room;
import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskRequest;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskResponse;
import com.Rothana.hotel_booking_system.features.HousekeepingTask.dto.HousekeepingTaskUpdateRequest;
import com.Rothana.hotel_booking_system.features.room.RoomRepository;
import com.Rothana.hotel_booking_system.features.user.UserRepository;
import com.Rothana.hotel_booking_system.mapper.HousekeepingTaskMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class HousekeepingTaskServiceImpl implements HousekeepingTaskService {

    private final HousekeepingTaskRepository repository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final HousekeepingTaskMapper mapper;

    private static final String ROOM_STATUS_MAINTENANCE = "MAINTENANCE";
    private static final String ROOM_STATUS_DIRTY = "DIRTY";
    private static final String ROOM_STATUS_CLEANING = "CLEANING";
    private static final String ROOM_STATUS_AVAILABLE = "AVAILABLE";

    @Override
    public HousekeepingTaskResponse create(HousekeepingTaskRequest request) {
        if (request.roomId() == null) throw new IllegalArgumentException("roomId is required");
        if (request.assignedToId() == null) throw new IllegalArgumentException("assignedToId is required");
        if (request.taskDate() == null) throw new IllegalArgumentException("taskDate is required");

        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(() -> new RuntimeException("Room not found: " + request.roomId()));

        User staff = userRepository.findById(request.assignedToId())
                .orElseThrow(() -> new RuntimeException("User not found: " + request.assignedToId()));

        HousekeepingTask entity = mapper.toEntity(request);
        entity.setRoom(room);
        entity.setAssignedTo(staff);

        if (entity.getStatus() == null) entity.setStatus(HousekeepingStatus.PENDING);

        // ✅ FEATURE: set room status when task is created (if not maintenance)
        if (!ROOM_STATUS_MAINTENANCE.equalsIgnoreCase(room.getStatus())) {
            if (entity.getStatus() == HousekeepingStatus.PENDING) {
                room.setStatus(ROOM_STATUS_DIRTY);
            } else if (entity.getStatus() == HousekeepingStatus.IN_PROGRESS) {
                room.setStatus(ROOM_STATUS_CLEANING);
            } else if (entity.getStatus() == HousekeepingStatus.DONE) {
                room.setStatus(ROOM_STATUS_AVAILABLE);
            }
            roomRepository.save(room);
        }

        HousekeepingTask saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public HousekeepingTaskResponse findById(Integer id) {
        HousekeepingTask task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("HousekeepingTask not found: " + id));
        return mapper.toResponse(task);
    }

    @Override
    public List<HousekeepingTaskResponse> findAll() {
        return repository.findAll().stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<HousekeepingTaskResponse> findByRoom(Integer roomId) {
        return repository.findAllByRoom_Id(roomId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<HousekeepingTaskResponse> findByAssignedTo(Integer userId) {
        return repository.findAllByAssignedTo_Id(userId).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<HousekeepingTaskResponse> findByDate(LocalDate date) {
        return repository.findAllByTaskDate(date).stream().map(mapper::toResponse).toList();
    }

    @Override
    public List<HousekeepingTaskResponse> findByStatus(HousekeepingStatus status) {
        return repository.findAllByStatus(status).stream().map(mapper::toResponse).toList();
    }

    @Override
    public HousekeepingTaskResponse update(Integer id, HousekeepingTaskUpdateRequest request) {
        HousekeepingTask task = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("HousekeepingTask not found: " + id));

        if (request.assignedToId() != null) {
            User staff = userRepository.findById(request.assignedToId())
                    .orElseThrow(() -> new RuntimeException("User not found: " + request.assignedToId()));
            task.setAssignedTo(staff);
        }

        if (request.taskDate() != null) task.setTaskDate(request.taskDate());
        if (request.remarks() != null) task.setRemarks(request.remarks());

        // ✅ update status + update room status based on housekeeping status
        if (request.status() != null) {
            task.setStatus(request.status());

            Room room = task.getRoom();

            // If room is under maintenance, DO NOT change it from housekeeping
            if (!ROOM_STATUS_MAINTENANCE.equalsIgnoreCase(room.getStatus())) {

                if (request.status() == HousekeepingStatus.PENDING) {
                    room.setStatus(ROOM_STATUS_DIRTY);
                } else if (request.status() == HousekeepingStatus.IN_PROGRESS) {
                    room.setStatus(ROOM_STATUS_CLEANING);
                } else if (request.status() == HousekeepingStatus.DONE) {
                    room.setStatus(ROOM_STATUS_AVAILABLE);
                }

                roomRepository.save(room);
            }
        }

        HousekeepingTask saved = repository.save(task);
        return mapper.toResponse(saved);
    }

    @Override
    public void delete(Integer id) {
        if (!repository.existsById(id)) throw new RuntimeException("HousekeepingTask not found: " + id);
        repository.deleteById(id);
    }
}
