package com.Rothana.hotel_booking_system.features.telegram;

import com.Rothana.hotel_booking_system.entity.TelegramLinkCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TelegramLinkCodeRepository extends JpaRepository<TelegramLinkCode, Integer> {
    Optional<TelegramLinkCode> findByCodeAndUsedFalse(String code);
}

