package com.Rothana.hotel_booking_system.features.auth;

import com.Rothana.hotel_booking_system.entity.UserVerification;
import com.Rothana.hotel_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UserVerificationRepository extends JpaRepository<UserVerification, Integer> {
    Optional<UserVerification> findByUserAndVerifiedCode(User user, String verifiedCode);
    Optional<UserVerification> findByUser(User user);
}