package com.Rothana.hotel_booking_system.security;

import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class AuthCookieUtil {

    // In production set true (HTTPS)
    private final boolean secure = false; // change to true in production

    public ResponseCookie accessTokenCookie(String token, Duration maxAge) {
        return ResponseCookie.from("access_token", token)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(maxAge)
                .build();
    }

    // Scope refresh cookie only to refresh endpoint (good security)
    public ResponseCookie refreshTokenCookie(String token, Duration maxAge) {
        return ResponseCookie.from("refresh_token", token)
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/api/v1/auth/refresh-token")
                .maxAge(maxAge)
                .build();
    }

    public ResponseCookie clearAccessCookie() {
        return ResponseCookie.from("access_token", "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/")
                .maxAge(0)
                .build();
    }

    public ResponseCookie clearRefreshCookie() {
        return ResponseCookie.from("refresh_token", "")
                .httpOnly(true)
                .secure(secure)
                .sameSite("Lax")
                .path("/api/v1/auth/refresh-token")
                .maxAge(0)
                .build();
    }
}