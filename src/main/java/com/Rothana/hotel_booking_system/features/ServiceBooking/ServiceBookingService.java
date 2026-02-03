package com.Rothana.hotel_booking_system.features.ServiceBooking;

import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingCreateRequest;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingResponse;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingUpdateRequest;

import java.util.List;

public interface ServiceBookingService {

    ServiceBookingResponse create(ServiceBookingCreateRequest  request);
    ServiceBookingResponse update(Integer id , ServiceBookingUpdateRequest request);
    ServiceBookingResponse findById(Integer id);
    List<ServiceBookingResponse> findAll();
    void delete(Integer id);
}
