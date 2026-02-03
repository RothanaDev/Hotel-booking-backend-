package com.Rothana.hotel_booking_system.features.ServiceBooking;

import com.Rothana.hotel_booking_system.entity.Booking;
import com.Rothana.hotel_booking_system.entity.BookingService;
import com.Rothana.hotel_booking_system.entity.HotelService;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingCreateRequest;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingResponse;
import com.Rothana.hotel_booking_system.features.ServiceBooking.dto.ServiceBookingUpdateRequest;
import com.Rothana.hotel_booking_system.features.booking.BookingRepository;
import com.Rothana.hotel_booking_system.features.service.ServiceRepository;
import com.Rothana.hotel_booking_system.mapper.ServiceBookingMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class ServiceBookingImpl implements  ServiceBookingService{
    private  final ServiceBookingRepository serviceBookingRepository;
    private  final ServiceBookingMapper serviceBookingMapper;
    private  final BookingRepository bookingRepository;
    private  final ServiceRepository serviceRepository;
    @Override
    public ServiceBookingResponse create(ServiceBookingCreateRequest request) {

        Booking booking = bookingRepository.findById(request.bookingId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking Not Found"));

        HotelService hotelService = serviceRepository.findById(request.serviceId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Service Not Found"));

        BookingService service = serviceBookingMapper.fromBookingServiceCreateRequest(request);
        service.setBooking(booking);
        service.setService(hotelService);

        // ✅ Calculate total amount = price * quantity
        BigDecimal total = hotelService.getPrice()
                .multiply(BigDecimal.valueOf(request.quantity()));
        service.setTotalAmount(total);

        serviceBookingRepository.save(service);

        return serviceBookingMapper.toBookingServiceResponse(service);
    }

    @Override
    public ServiceBookingResponse update(Integer id, ServiceBookingUpdateRequest request) {
        BookingService service = serviceBookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        // Update quantity if provided
        if (request.quantity() != null) {
            service.setQuantity(request.quantity());

            // Recalculate total
            BigDecimal total = service.getService().getPrice()
                    .multiply(BigDecimal.valueOf(request.quantity()));
            service.setTotalAmount(total);
        }

        serviceBookingRepository.save(service);

        return serviceBookingMapper.toBookingServiceResponse(service);
    }

    @Override
    public ServiceBookingResponse findById(Integer id) {
        BookingService service = serviceBookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        return serviceBookingMapper.toBookingServiceResponse(service);
    }

    @Override
    public List<ServiceBookingResponse> findAll() {
        List<BookingService> bookingServices = serviceBookingRepository.findAll();
        return serviceBookingMapper.toBookingServiceResponseList(bookingServices);
    }

    @Override
    public void delete(Integer id) {
        BookingService service = serviceBookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));
        serviceBookingRepository.delete(service);

    }
}
