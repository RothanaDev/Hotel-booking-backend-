package com.Rothana.hotel_booking_system.features.Inventory;

import com.Rothana.hotel_booking_system.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventorRepository extends JpaRepository<Inventory , Integer> {
    Optional<Inventory> findById(Integer id);
}
