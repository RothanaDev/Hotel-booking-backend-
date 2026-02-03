package com.Rothana.hotel_booking_system.features.service;

import com.Rothana.hotel_booking_system.features.service.dto.ServiceResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/services")
@AllArgsConstructor
public class ServiceController {
    private  final ServiceService service;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ServiceResponse create(
            @RequestParam("serviceName") String serviceName,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("category") String category,
            @RequestParam("image") MultipartFile image
    ) {
        return service.create(serviceName, description, price, category, image);
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ServiceResponse  update(
            @PathVariable("id") Integer id,
            @RequestParam("serviceName") String serviceName,
            @RequestParam("description") String description,
            @RequestParam("price") BigDecimal price,
            @RequestParam("category") String category,
            @RequestParam(value = "image", required = false) MultipartFile image

    ) {
        return  service.update(id, serviceName, description, price, category, image);
    }
    @GetMapping("/findById/{id}")
    public ServiceResponse findById(@PathVariable("id") Integer id) {
        return service.findById(id);
    }
    @GetMapping("/findAll")
    public List<ServiceResponse> findAll() {
        return service.findAll();
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable("id") Integer id) {
        service.deleteById(id);
    }
}
