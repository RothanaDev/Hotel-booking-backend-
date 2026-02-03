package com.Rothana.hotel_booking_system.entity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "services")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "service_name", nullable = false)
    private String serviceName;

    private String description;

    private BigDecimal price;

    private String category;
    private String image;

    @OneToMany(mappedBy = "service", cascade = CascadeType.ALL)
    private List<BookingService> bookingServices;
}