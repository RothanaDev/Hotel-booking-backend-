package com.Rothana.hotel_booking_system.features.RoomType;

import com.Rothana.hotel_booking_system.entity.RoomType;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeCreateRequest;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeResponse;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeUpdateRequest;
import com.Rothana.hotel_booking_system.mapper.RoomTypeMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@AllArgsConstructor

public class RoomTypeServiceImpl implements RoomTypeService {
    private final RoomTypeRepository roomTypeRepository;
    private final RoomTypeMapper roomTypeMapper;

    @Override
    public RoomTypeResponse create(RoomTypeCreateRequest request) {
        RoomType roomType = roomTypeMapper.fromRoomTypeCreateRequest(request);
       roomType = roomTypeRepository.save(roomType);
        return roomTypeMapper.toRoomTypeResponse(roomType);
    }

    @Override
    public RoomTypeResponse update(Integer id, RoomTypeUpdateRequest request) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not found"));
        roomType.setTypeName(request.typeName());
        roomType.setDescription(request.description());
        roomType.setPrice(request.price());
        roomType = roomTypeRepository.save(roomType);
        return roomTypeMapper.toRoomTypeResponse(roomType);
    }

    @Override
    public RoomTypeResponse findById(Integer id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not found"));
        return roomTypeMapper.toRoomTypeResponse(roomType);
    }

    @Override
    public List<RoomTypeResponse> findAll() {
        List<RoomType> roomTypes = roomTypeRepository.findAll();

        return roomTypeMapper.toRoomTypeResponseList(roomTypes);
    }

    @Override
    public void delete(Integer id) {
        RoomType roomType = roomTypeRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not found"));
        roomTypeRepository.delete(roomType);

    }
}
