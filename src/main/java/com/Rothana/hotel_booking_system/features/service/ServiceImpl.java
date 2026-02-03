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


@AllArgsConstructor
@Service
public class ServiceImpl implements ServiceService {

    private  final  ServiceRepository serviceRepository;
    private  final ServiceMapper serviceMapper;
    private  final FileUploadService fileUploadService;

    @Override
    public ServiceResponse create(String serviceName , String description , BigDecimal price , String category , MultipartFile image) {

        FileUploadResponse upload =fileUploadService.upload(image);
        HotelService service = new HotelService();
        service.setServiceName(serviceName);
        service.setDescription(description);
        service.setPrice(price);
        service.setCategory(category);
        service.setImage(upload.url());

        return serviceMapper.toServiceResponse(serviceRepository.save(service));
    }

    @Override
    public ServiceResponse update(Integer id ,String serviceName , String description ,  BigDecimal price , String category , MultipartFile image) {
        HotelService service = serviceRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Id Not found"));
        service.setServiceName(serviceName);
        service.setDescription(description);
        service.setPrice(price);
        service.setCategory(category);

        // Update image only if a new one is provided
        if (image != null && !image.isEmpty()) {
            FileUploadResponse upload = fileUploadService.upload(image);
            service.setImage(upload.url());
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
