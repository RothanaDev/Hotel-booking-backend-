package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.BookingService;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingCreateRequest;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingResponse;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingUpdateRequest;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceBookingMapper {

    @Mapping(source = "service", target = "serviceResponse")
    ServiceBookingResponse toBookingServiceResponse(BookingService bookingService);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    BookingService fromBookingServiceCreateRequest(ServiceBookingCreateRequest request);

    List<ServiceBookingResponse> toBookingServiceResponseList(List<BookingService> bookingServices);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "service", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    void updateBookingServiceFromRequest(ServiceBookingUpdateRequest request,
                                         @MappingTarget BookingService bookingService);
}
