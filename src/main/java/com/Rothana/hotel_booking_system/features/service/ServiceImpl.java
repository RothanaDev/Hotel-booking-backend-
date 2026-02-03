package com.Rothana.hotel_booking_system.features.service;

import com.Rothana.hotel_booking_system.features.service.dto.ServiceCreateRequest;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceResponse;
import com.Rothana.hotel_booking_system.features.service.dto.ServiceUpdateRequest;
import com.Rothana.hotel_booking_system.entity.HotelService;
import com.Rothana.hotel_booking_system.fileupload.FileUploadResponse;
import com.Rothana.hotel_booking_system.fileupload.FileUploadService;
import com.Rothana.hotel_booking_system.mapper.ServiceMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


import java.math.BigDecimal;
import java.util.List;
import java.util.Map;


@AllArgsConstructor
@Service
public class ServiceImpl implements ServiceService {

    private  final  ServiceRepository serviceRepository;
    private  final ServiceMapper serviceMapper;
    private  final FileUploadService fileUploadService;

    @Override
    public ServiceResponse create(String serviceName , String description , BigDecimal price , String category , MultipartFile image) {

        String imageUrl = null;
        try {
            if (image != null && !image.isEmpty()) {
                Map<String, Object> upload = fileUploadService.upload(image);
                imageUrl = (String) upload.get("secure_url");
            }
        } catch (Exception e) {
            throw new RuntimeException("Upload image failed", e);
        }


        HotelService service = new HotelService();
        service.setServiceName(serviceName);
        service.setDescription(description);
        service.setPrice(price);
        service.setCategory(category);
        service.setImage(imageUrl);

        return serviceMapper.toServiceResponse(serviceRepository.save(service));

    }

    @Override
    public ServiceResponse update(Integer id ,String serviceName , String description ,  BigDecimal price , String category , MultipartFile image) {
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Id Not found"));

        // update only if provided
        if (serviceName != null && !serviceName.isBlank()) {
            service.setServiceName(serviceName);
        }

        if (description != null && !description.isBlank()) {
            service.setDescription(description);
        }

        if (price != null) {
            service.setPrice(price);
        }

        if (category != null && !category.isBlank()) {
            service.setCategory(category);
        }

        // update image only if new file provided
        if (image != null && !image.isEmpty()) {
            try {
                Map<String, Object> upload = fileUploadService.upload(image);
                String imageUrl = (String) upload.get("secure_url");
                service.setImage(imageUrl);
            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Upload image failed", e);
            }
        }

        return serviceMapper.toServiceResponse(serviceRepository.save(service));
    }

    @Override
    public ServiceResponse findById(Integer id) {
        HotelService service =serviceRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not found"));
        return serviceMapper.toServiceResponse(service);
    }

    @Override
    public List<ServiceResponse> findAll() {
        List<HotelService> hotelServices = serviceRepository.findAll();
        return serviceMapper.toServiceResponseList(hotelServices);
    }

    @Override
    public void deleteById(Integer id) {
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not found"));
        serviceRepository.delete(service);

    }
}
