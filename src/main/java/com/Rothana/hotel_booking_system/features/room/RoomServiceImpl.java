package com.Rothana.hotel_booking_system.features.room;

import com.Rothana.hotel_booking_system.entity.Room;
import com.Rothana.hotel_booking_system.entity.RoomType;
import com.Rothana.hotel_booking_system.features.RoomType.RoomTypeRepository;
import com.Rothana.hotel_booking_system.features.room.dto.RoomResponse;
import com.Rothana.hotel_booking_system.fileupload.FileUploadResponse;
import com.Rothana.hotel_booking_system.fileupload.FileUploadService;
import com.Rothana.hotel_booking_system.mapper.RoomMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor
public class RoomServiceImpl implements RoomService {
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;
    private  final FileUploadService  fileUploadService;
    private  final RoomTypeRepository  roomTypeRepository;


    @Override
    public RoomResponse create(Integer roomTypeId, String status, MultipartFile image) {
        RoomType roomType = roomTypeRepository.findById(roomTypeId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RoomType Id Not Found"));

        FileUploadResponse upload = fileUploadService.upload(image);

        Room room = new Room();
        room.setRoomType(roomType);
        room.setStatus(status != null ? status : "available");
        room.setImage(upload.url());
        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse update(Integer id, Integer roomTypeId, String status, MultipartFile image) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Id Not Found"));

        if (roomTypeId != null) {
            RoomType roomType = roomTypeRepository.findById(roomTypeId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RoomType Id Not Found"));
            room.setRoomType(roomType);
        }

        if (status != null) {
            room.setStatus(status);
        }

        if (image != null && !image.isEmpty()) {
            FileUploadResponse upload = fileUploadService.upload(image);
            room.setImage(upload.url());
        }

        return roomMapper.toRoomResponse(roomRepository.save(room));
    }

    @Override
    public RoomResponse findById(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Id Not Found"));

        return roomMapper.toRoomResponse(room);
    }

    @Override
    public List<RoomResponse> findAll() {
        return roomRepository.findAll()
                .stream()
                .map(roomMapper::toRoomResponse)
                .toList();
    }

    @Override
    public void delete(Integer id) {
        Room room = roomRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Id Not Found"));

        roomRepository.delete(room);
    }
}
