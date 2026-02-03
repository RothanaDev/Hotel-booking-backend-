package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.HotelService;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceCreateRequest;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceResponse;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceUpdateRequest;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ServiceMapper {
    ServiceResponse toServiceResponse(HotelService service);
    List<ServiceResponse> toServiceResponseList(List<HotelService> services);
    HotelService fromServiceCreateRequest(ServiceCreateRequest serviceCreateRequest);
    void updateServiceFromRequest(ServiceUpdateRequest  serviceUpdateRequest , @MappingTarget HotelService service);

}
