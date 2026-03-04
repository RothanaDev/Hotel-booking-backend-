package com.Rothana.hotel_booking_system.features.telegram;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseConstraintFixer implements CommandLineRunner {

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void run(String... args) {
        try {
            System.out.println("🔧 Attempting to drop unique constraint on telegram_chat_id...");
            entityManager.createNativeQuery("ALTER TABLE users DROP CONSTRAINT IF EXISTS uk6t9hfgo7gxvw604uq3sqkkbxq")
                    .executeUpdate();
            System.out.println("✅ Constraint dropped successfully (or did not exist).");
        } catch (Exception e) {
            System.err.println("⚠️ Could not drop constraint: " + e.getMessage());
        }
    }
}
