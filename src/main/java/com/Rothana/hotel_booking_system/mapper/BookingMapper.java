package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.Booking;
import com.Rothana.hotel_booking_system.features.booking.dto.BookingCreateRequest;
import com.Rothana.hotel_booking_system.features.booking.dto.BookingResponse;
import com.Rothana.hotel_booking_system.features.booking.dto.BookingUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring", uses = { ServiceBookingMapper.class })
public interface BookingMapper {

    @Mapping(source = "user", target = "userResponse")
    @Mapping(source = "room", target = "roomResponse")
    @Mapping(source = "bookingServices", target = "bookingServices")
    BookingResponse toBookingResponse(Booking booking);

    List<BookingResponse> toBookingResponseList(List<Booking> bookings);

    Booking fromBookingCreateRequest(BookingCreateRequest bookingCreateRequest);

    void updateBookingFromRequest(BookingUpdateRequest bookingUpdateRequest,
            @MappingTarget Booking booking);
}
