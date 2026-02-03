package com.Rothana.hotel_booking_system.features.service;

import com.Rothana.hotel_booking_system.features.service.dto.ServiceCreateRequest;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceResponse;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

public interface ServiceService {

    ServiceResponse create(String serviceName , String description ,  BigDecimal price , String category , MultipartFile image);
    ServiceResponse update(Integer id ,String serviceName , String description ,  BigDecimal price , String category , MultipartFile image);

    ServiceResponse findById(Integer id);
    List<ServiceResponse> findAll();
    void deleteById(Integer id);
}
