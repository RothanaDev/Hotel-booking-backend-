package com.Rothana.hotel_booking_system.features.user;

import com.Rothana.hotel_booking_system.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query(""" 
            SELECT r
            FROM Role r
            WHERE r.name = 'USER'

         """)
    Role findRoleUser();

    @Query("SELECT r FROM Role  as r WHERE r.name = 'ADMIN'")
    Role findRoleAdmin();

    @Query("SELECT r FROM Role  as r WHERE r.name ='STAFF'")
    Role findByCustomer();
}
