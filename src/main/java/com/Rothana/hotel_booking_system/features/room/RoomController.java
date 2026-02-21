package com.Rothana.hotel_booking_system.features.room;

import com.Rothana.hotel_booking_system.features.room.dto.RoomCreateRequest;
import com.Rothana.hotel_booking_system.features.room.dto.RoomResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/rooms")
@AllArgsConstructor
public class RoomController {
    private  final RoomService roomService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RoomResponse create(
            @RequestParam("roomTypeId") Integer roomTypeId,
            @RequestParam("status") String status,
            @RequestParam("image") MultipartFile image
    ) {
        return roomService.create(roomTypeId, status, image);
    }
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RoomResponse update(
            @PathVariable Integer id,
            @RequestParam(value = "roomTypeId", required = false) Integer roomTypeId,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) {
        return roomService.update(id, roomTypeId, status, image);
    }
    @GetMapping("/{id}")
    public   RoomResponse findById(@PathVariable("id") Integer id) {
        return roomService.findById(id);
    }
    @GetMapping()
    public List<RoomResponse> findAll() {
        return roomService.findAll();
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void  delete(@PathVariable("id") Integer id) {
        roomService.delete(id);
    }


}
