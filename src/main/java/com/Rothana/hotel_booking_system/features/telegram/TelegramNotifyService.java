package com.Rothana.hotel_booking_system.features.telegram;

import com.Rothana.hotel_booking_system.entity.Booking;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class TelegramNotifyService {

    private final HotelBookingTelegramBot bot;

    public void sendToChat(Long chatId, String text) {
        if (chatId == null)
            return; // user not linked yet

        try {
            bot.execute(SendMessage.builder()
                    .chatId(chatId.toString())
                    .text(text)
                    .parseMode("Markdown")
                    .build());
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendBookingNotification(Booking booking) {
        if (booking.getUser() == null || booking.getUser().getTelegramChatId() == null) {
            return;
        }

        Long chatId = booking.getUser().getTelegramChatId();

        String text = String.format(
                "✅ *Payment Success!*\n\n" +
                        "🆔 *Booking ID:* #%d\n" +
                        "🚪 *Room ID:* %d\n" +
                        "💰 *Room Price:* $%.2f / night\n" +
                        "💵 *Total Paid:* $%.2f\n" +
                        "📅 *Check-in:* %s\n" +
                        "📅 *Check-out:* %s\n\n" +
                        "Thank you for booking with us! 😊",
                booking.getId(),
                booking.getRoom().getId(),
                booking.getRoom().getRoomType().getPrice(),
                booking.getAmount(),
                booking.getCheckin(),
                booking.getCheckout());

        String imageUrl = booking.getRoom().getImage();

        if (imageUrl != null && !imageUrl.isBlank()) {
            try {
                bot.execute(SendPhoto.builder()
                        .chatId(chatId.toString())
                        .photo(new InputFile(imageUrl))
                        .caption(text)
                        .parseMode("Markdown")
                        .build());
            } catch (TelegramApiException e) {
                // If photo sending fails (e.g. invalid URL), fallback to plain text message
                sendToChat(chatId, text);
            }
        } else {
            sendToChat(chatId, text);
        }
    }
}
