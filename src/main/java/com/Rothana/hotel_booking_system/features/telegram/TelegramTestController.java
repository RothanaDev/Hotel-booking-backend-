package com.Rothana.hotel_booking_system.features.telegram;

import com.Rothana.hotel_booking_system.features.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TelegramTestController {
    private final TelegramNotifyService telegramNotifyService;
    private final UserRepository userRepository;

    @GetMapping("/api/v1/telegram/test/{email}")
    public String test(@PathVariable String email) {
        var user = userRepository.findByEmail(email).orElseThrow();
        telegramNotifyService.sendToChat(user.getTelegramChatId(), "Hello from backend ✅");
        return "sent";
    }
}
