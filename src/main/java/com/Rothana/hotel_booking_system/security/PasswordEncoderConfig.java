package com.Rothana.hotel_booking_system.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordEncoderConfig {
    @Bean
    PasswordEncoder CongfigPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
