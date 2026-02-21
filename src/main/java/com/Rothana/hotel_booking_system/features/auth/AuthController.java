package com.Rothana.hotel_booking_system.features.auth;

import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.features.auth.dto.*;
import com.Rothana.hotel_booking_system.features.user.UserRepository;
import com.Rothana.hotel_booking_system.security.AuthCookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthCookieUtil authCookieUtil;
    private  final UserRepository userRepository;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginPublicResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        AuthResponse tokens = authService.login(loginRequest);

        var accessCookie  = authCookieUtil.accessTokenCookie(tokens.accessToken(), Duration.ofMinutes(15));
        var refreshCookie = authCookieUtil.refreshTokenCookie(tokens.refreshToken(), Duration.ofDays(7));

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                .body(new LoginPublicResponse(tokens.id(), tokens.tokenType()));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(HttpServletRequest request) {
        String refreshToken = readCookie(request, "refresh_token");
        if (refreshToken == null || refreshToken.isBlank()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing refresh token");
        }

        AuthResponse tokens = authService.refreshToken(new RefreshTokenRequest(refreshToken));

        var accessCookie = authCookieUtil.accessTokenCookie(tokens.accessToken(), Duration.ofMinutes(15));

        ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessCookie.toString());

        // rotate refresh cookie only if refresh changed
        if (tokens.refreshToken() != null && !tokens.refreshToken().equals(refreshToken)) {
            var refreshCookie = authCookieUtil.refreshTokenCookie(tokens.refreshToken(), Duration.ofDays(7));
            builder.header(HttpHeaders.SET_COOKIE, refreshCookie.toString());
        }

        return builder.body("OK");
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authCookieUtil.clearAccessCookie().toString())
                .header(HttpHeaders.SET_COOKIE, authCookieUtil.clearRefreshCookie().toString())
                .body("Logged out");
    }

    @GetMapping("/{id}")
    public UserResponse findById(@PathVariable Integer id) {
        return authService.findById(id);
    }

    @GetMapping("/all")
    public List<UserResponse> findAll() {
        return authService.findAll();
    }
    @GetMapping("/me")
    public MeResponse me(Authentication authentication) {

        // 1️⃣ Check authentication exists
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        // 2️⃣ Get JWT principal
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Jwt jwt)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid token");
        }

        // 3️⃣ Read email from JWT subject (CORRECT WAY)
        String email = jwt.getSubject();
        if (email == null || email.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing email in token");
        }

        // 4️⃣ Load user from database
        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // 5️⃣ Read role from token claim
        String role = jwt.getClaimAsString("role");

        // 6️⃣ Return response
        return new MeResponse(
                user.getId(),
                user.getEmail(),
                role
        );
    }

    private String readCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (name.equals(c.getName())) return c.getValue();
        }
        return null;
    }

    public record LoginPublicResponse(Integer id, String tokenType) {}
}