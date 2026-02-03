package com.Rothana.hotel_booking_system.features.ServiceBooking;

import com.Rothana.hotel_booking_system.entity.BookingService;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingCreateRequest;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingResponse;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingUpdateRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/booking_services")
@AllArgsConstructor
public class ServiceBookingController {
    private  final ServiceBookingService serviceBookingService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public ServiceBookingResponse create(@Valid @RequestBody ServiceBookingCreateRequest request) {
        return serviceBookingService.create(request);
    }
    @PutMapping("/update/{id}")
    public ServiceBookingResponse update(@PathVariable("id") Integer id, @Valid @RequestBody ServiceBookingUpdateRequest request) {
     return serviceBookingService.update(id, request);
    }
    @GetMapping("/findAll")
     public List<ServiceBookingResponse> findAll() {
        return serviceBookingService.findAll();
     }
     @GetMapping("/findById/{id}")
    public ServiceBookingResponse findById(@PathVariable("id") Integer id) {
        return serviceBookingService.findById(id);
     }
     @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable("id") Integer id) {
        serviceBookingService.delete(id);
     }
}
