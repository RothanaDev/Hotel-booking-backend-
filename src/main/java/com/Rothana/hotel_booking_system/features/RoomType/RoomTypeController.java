package com.Rothana.hotel_booking_system.features.RoomType;

import com.Rothana.hotel_booking_system.entity.RoomType;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeCreateRequest;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeResponse;
import com.Rothana.hotel_booking_system.features.RoomType.dto.RoomTypeUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roomTypes")
@AllArgsConstructor
public class RoomTypeController {
    private  final RoomTypeService roomTypeService;

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public RoomTypeResponse create(@Valid  @RequestBody RoomTypeCreateRequest  roomTypeCreateRequest ){
        return roomTypeService.create(roomTypeCreateRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN','STAFF')")
    @PutMapping("/update/{id}")
    public RoomTypeResponse update(@PathVariable("id") Integer id, @Valid  @RequestBody RoomTypeUpdateRequest roomTypeUpdateRequest){
        return roomTypeService.update(id, roomTypeUpdateRequest);
    }
    @GetMapping("/findById/{id}")
    public RoomTypeResponse findById(@PathVariable("id") Integer id){
        return roomTypeService.findById(id);
    }
    @GetMapping("/findAll")
    public List<RoomTypeResponse> findAll(){
        return roomTypeService.findAll();
    }
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void  delete(@PathVariable("id") Integer id){
        roomTypeService.delete(id);
    }

}
