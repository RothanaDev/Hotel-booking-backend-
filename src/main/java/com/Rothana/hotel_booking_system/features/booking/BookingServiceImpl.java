package com.Rothana.hotel_booking_system.features.booking;

import com.Rothana.hotel_booking_system.entity.Booking;
import com.Rothana.hotel_booking_system.entity.Room;
import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.features.booking.dto.BookingCreateRequest;
import com.Rothana.hotel_booking_system.features.booking.dto.BookingResponse;
import com.Rothana.hotel_booking_system.features.booking.dto.BookingUpdateRequest;
import com.Rothana.hotel_booking_system.features.room.RoomRepository;
import com.Rothana.hotel_booking_system.features.user.UserRepository;
import com.Rothana.hotel_booking_system.mapper.BookingMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class BookingServiceImpl implements BookingService {
    private  final BookingMapper bookingMapper;
    private final BookingRepository bookingRepository;
    private final RoomRepository roomRepository;
    private  final UserRepository userRepository;


    @Override
    public BookingResponse create(BookingCreateRequest request) {



        Room room = roomRepository.findById(request.roomId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "Room Not Found"));

        User user = userRepository.findById(request.userId())
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"User Not found"));

        Booking booking = bookingMapper.fromBookingCreateRequest(request);
        booking.setRoom(room);
        booking.setUser(user);
        booking.setTotalNumOfGuest(request.numOfChildren() + request.numOfChildren());
        booking.setStatus("pending");
        booking.setCreatedAt(java.time.LocalDateTime.now());

        long nights = ChronoUnit.DAYS.between(request.checkin(), request.checkout());
        BigDecimal roomTotal = room.getRoomType().getPrice().multiply(BigDecimal.valueOf(nights));
        booking.setAmount(roomTotal);


        Booking savedBooking = bookingRepository.save(booking);


        return bookingMapper.toBookingResponse(savedBooking);

    }

    @Override
    public BookingResponse update(Integer id, BookingUpdateRequest request) {

        // 1. Find the existing booking
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking Not Found"));

        // 2. Validate dates: check-out must be after check-in
        if (request.checkout().isBefore(request.checkin()) || request.checkout().isEqual(request.checkin())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Check-out date must be after check-in date");
        }

        // 3. Update booking fields
        booking.setCheckin(request.checkin());
        booking.setCheckout(request.checkout());
        booking.setNumOfAdults(request.numOfAdults());
        booking.setNumOfChildren(request.numOfChildren());
        booking.setTotalNumOfGuest(request.numOfAdults() + request.numOfChildren());
        booking.setStatus(request.status());
        booking.setUpdatedAt(java.time.LocalDateTime.now());

        // 4. Recalculate total amount
        long nights = ChronoUnit.DAYS.between(request.checkin(), request.checkout());

        // Room total
        BigDecimal roomTotal = booking.getRoom().getRoomType().getPrice()
                .multiply(BigDecimal.valueOf(nights));

        // Optional: add total from services if any
        BigDecimal servicesTotal = booking.getBookingServices().stream()
                .map(bs -> bs.getService().getPrice()
                        .multiply(BigDecimal.valueOf(bs.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        booking.setAmount(roomTotal.add(servicesTotal));

        // 5. Save updated booking
        Booking updatedBooking = bookingRepository.save(booking);

        // 6. Return response
        return bookingMapper.toBookingResponse(updatedBooking);
    }

    @Override
    public BookingResponse findById(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking Not Found"));
        return bookingMapper.toBookingResponse(booking);
    }

    @Override
    public List<BookingResponse> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        return bookingMapper.toBookingResponseList(bookings);
    }

    @Override
    public void delete(Integer id) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND,"Booking Not Found"));
        bookingRepository.delete(booking);
    }
}
