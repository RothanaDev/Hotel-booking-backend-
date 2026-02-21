package com.Rothana.hotel_booking_system.features.telegram;

import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.features.user.UserRepository;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Optional;

@Component
public class HotelBookingTelegramBot extends TelegramLongPollingBot {

    private final TelegramBotProperties props;
    private final UserRepository userRepository;

    public HotelBookingTelegramBot(
            TelegramBotProperties props,
            UserRepository userRepository
    ) {
        super(props.token());
        this.props = props;
        this.userRepository = userRepository;
    }

    @Override
    public String getBotUsername() {
        return props.username();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) return;

        Long chatId = update.getMessage().getChatId();
        String text = update.getMessage().getText().trim();

        // Debug (optional)
        System.out.println("Telegram received: " + text + " chatId=" + chatId);

        if (text.equals("/start")) {
            reply(chatId, """
                    Welcome 👋
                    Link your account:
                    /link your@email.com
                    """);
            return;
        }

        if (text.startsWith("/link")) {
            String[] parts = text.split("\\s+");
            if (parts.length != 2) {
                reply(chatId, "Usage: /link your@email.com");
                return;
            }
            handleLinkByEmail(chatId, parts[1]);
            return;
        }

        if (text.equals("/ping")) {
            reply(chatId, "pong ✅");
            return;
        }

        reply(chatId, "Unknown command. Try /start");
    }

    private void handleLinkByEmail(Long chatId, String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            reply(chatId, "No account found for: " + email + " ❌");
            return;
        }

        User user = userOpt.get();
        user.setTelegramChatId(chatId);
        userRepository.save(user);

        reply(chatId, "Linked ✅ Hello " + (user.getName() == null ? "" : user.getName()));
    }

    private void reply(Long chatId, String msg) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(msg)
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
