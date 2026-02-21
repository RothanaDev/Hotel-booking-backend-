package com.Rothana.hotel_booking_system.features.MaintenanceTicket;

import com.Rothana.hotel_booking_system.entity.MaintenanceStatus;
import com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto.MaintenanceTicketRequest;
import com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto.MaintenanceTicketResponse;
import com.Rothana.hotel_booking_system.features.MaintenanceTicket.dto.MaintenanceTicketUpdateRequest;

import java.util.List;

public interface MaintenanceTicketService {

    MaintenanceTicketResponse create(MaintenanceTicketRequest request);

    MaintenanceTicketResponse findById(Integer id);

    List<MaintenanceTicketResponse> findAll();

    List<MaintenanceTicketResponse> findByRoom(Integer roomId);

    List<MaintenanceTicketResponse> findByReportedBy(Integer userId);

    List<MaintenanceTicketResponse> findByStatus(MaintenanceStatus status);

    MaintenanceTicketResponse update(Integer id, MaintenanceTicketUpdateRequest request);

    void delete(Integer id);
}
