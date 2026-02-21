package com.Rothana.hotel_booking_system.features.MaintenanceTicket;

import com.Rothana.hotel_booking_system.entity.MaintenanceStatus;
import com.Rothana.hotel_booking_system.entity.MaintenanceTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaintenanceTicketRepository
        extends JpaRepository<MaintenanceTicket, Integer> {

    List<MaintenanceTicket> findAllByRoom_Id(Integer roomId);

    List<MaintenanceTicket> findAllByReportedBy_Id(Integer userId);

    List<MaintenanceTicket> findAllByStatus(MaintenanceStatus status);
}
