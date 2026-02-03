package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.BookingService;
import com.Rothana.hotel_booking_system.entity.RoomType;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingCreateRequest;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingResponse;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceBookingMapper {

    @Mapping(source = "booking", target = "bookingResponse")
    @Mapping(source = "service", target = "serviceResponse")
    ServiceBookingResponse toBookingServiceResponse(BookingService bookingService);
    BookingService fromBookingServiceCreateRequest(ServiceBookingCreateRequest  serviceBookingCreateRequest);
    List<ServiceBookingResponse> toBookingServiceResponseList(List<BookingService> bookingServices);

    void updateBookingServiceFromRequest(ServiceBookingUpdateRequest serviceBookingUpdateRequest, @MappingTarget BookingService bookingService);
}
