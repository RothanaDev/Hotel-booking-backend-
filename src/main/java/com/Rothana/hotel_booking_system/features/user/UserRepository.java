package com.Rothana.hotel_booking_system.features.user;

import com.Rothana.hotel_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);
    Boolean existsAllByEmail(String email);

    Optional<User> findByTelegramChatId(Long telegramChatId);
    boolean existsByTelegramChatId(Long telegramChatId);
}
