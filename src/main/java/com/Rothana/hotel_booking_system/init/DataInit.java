package com.Rothana.hotel_booking_system.init;

import com.Rothana.hotel_booking_system.entity.Role;
import com.Rothana.hotel_booking_system.features.user.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final RoleRepository roleRepository;

    @PostConstruct
    void init() {
        // Only initialize if roles don't exist
        if (roleRepository.count() == 0) {
            Role user = new Role();
            user.setName("USER");  // Changed from "User" to "USER"

            Role admin = new Role();
            admin.setName("ADMIN");

            Role staff = new Role();
            staff.setName("STAFF");  // Changed from "Staff" to "STAFF"

            roleRepository.saveAll(List.of(user, admin, staff));

            System.out.println("✅ Roles initialized: USER, ADMIN, STAFF");
        } else {
            System.out.println("ℹ️ Roles already exist in database");
        }
    }
}