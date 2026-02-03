package com.Rothana.hotel_booking_system.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "booking_service")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookingService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "booking_id", nullable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private HotelService service;

    private Integer quantity ;

    @Column(name = "total_amount")
    private BigDecimal totalAmount;
}