package com.Rothana.hotel_booking_system.features.auth;

import com.Rothana.hotel_booking_system.entity.Role;
import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.entity.UserVerification;
import com.Rothana.hotel_booking_system.features.auth.dto.*;
import com.Rothana.hotel_booking_system.features.telegram.TelegramNotifyService;
import com.Rothana.hotel_booking_system.features.user.RoleRepository;
import com.Rothana.hotel_booking_system.features.user.UserRepository;
import com.Rothana.hotel_booking_system.mapper.UserMapper;
import com.Rothana.hotel_booking_system.util.RandomUtil;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class AuthServiceImpl implements AuthService {
        private final UserMapper userMapper;
        private final UserRepository userRepository;
        private final RoleRepository roleRepository;
        private final UserVerificationRepository userVerificationRepository;
        private final PasswordEncoder passwordEncoder;

        private final JwtAuthenticationProvider jwtAuthenticationProvider;
        private final DaoAuthenticationProvider daoAuthenticationProvider;

        private final JwtEncoder accessTokenJwtEncoder;
        private final JwtEncoder refreshTokenEncoder;

        private final TelegramNotifyService telegramNotifyService;

        private final JavaMailSender javaMailSender;


        @Value("${spring.mail.username}")
        private  String adminEmail;

        @Override
        public void sendVerification(String email) throws MessagingException  {


            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

            String code = RandomUtil.random6Digits();

            UserVerification verification = userVerificationRepository.findByUser(user)
                    .orElseGet(UserVerification::new);

            verification.setUser(user);
            verification.setVerifiedCode(code);
            verification.setExpirationTime(LocalTime.now().plusMinutes(1)); // better: use Instant/LocalDateTime

            userVerificationRepository.save(verification);

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
            helper.setTo(email);
            helper.setFrom(adminEmail);
            helper.setSubject("User verification");
            helper.setText(code);

            javaMailSender.send(message);

        }

    @Override
    public void resendVerification(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        UserVerification userVerification = userVerificationRepository.findByUser(user)
                .orElseGet(UserVerification::new);

        String secure6Digit = RandomUtil.random6Digits();

        userVerification.setUser(user);
        userVerification.setVerifiedCode(secure6Digit);
        userVerification.setExpirationTime(LocalTime.now().plusMinutes(1)); // better: Instant/LocalDateTime

        userVerificationRepository.save(userVerification);

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");
        helper.setTo(email);
        helper.setFrom(adminEmail);
        helper.setSubject("User verification");
        helper.setText(secure6Digit);

        javaMailSender.send(message);


    }
    @Override
    public void verify(VerificationRequest verificationRequest) {
        //Validate email
        User user = userRepository.findByEmail(verificationRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));
        //Validate VerifiedCode
        UserVerification userVerification = userVerificationRepository
                .findByUserAndVerifiedCode(user , verificationRequest.verifiedCode())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"User not found"));

        if (LocalTime.now().isAfter(userVerification.getExpirationTime())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Verified code  has expired");
        }
        user.setVerified(true);
        userRepository.save(user);

        userVerificationRepository.delete(userVerification);

    }

    @Override
        public RegisterResponse register(RegisterRequest registerRequest) {
                // Validate email
                if (userRepository.existsAllByEmail(registerRequest.email())) {
                        throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
                }

                User user = userMapper.fromRegisterRequest(registerRequest);
                user.setPassword(passwordEncoder.encode(user.getPassword()));

                Role roleUser = roleRepository.findRoleUser();
                if (roleUser == null) {
                        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                                        "Role 'USER' not found in database. Please ensure roles are initialized.");
                }
                user.setRoles(List.of(roleUser));

                userRepository.save(user);

                // Notify via Telegram
                telegramNotifyService.sendRegistrationNotification(user);

                // Generate automatic linking URL
                String telegramLinkUrl = telegramNotifyService.generateLinkingUrl(user);

                return RegisterResponse.builder()
                                .message("You have registered successfully!")
                                .name(user.getName())
                                .email(user.getEmail())
                                .phoneNumber(user.getPhoneNumber())
                                .telegramLinkUrl(telegramLinkUrl)
                                .build();
        }

        @Override
        public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

                String refreshToken = refreshTokenRequest.refreshToken();
                // Authentication client with refresh Token
                Authentication auth = new BearerTokenAuthenticationToken(refreshToken);
                auth = jwtAuthenticationProvider.authenticate(auth);

                Jwt jwt = (Jwt) auth.getPrincipal();

                // Generate JWT Token by Encoder
                // 1 . Define ClaimSets(Payload)
                Instant now = Instant.now();
                JwtClaimsSet jwtAccessClaimsSet = JwtClaimsSet.builder()
                                .id(jwt.getId())
                                .subject(auth.getName())
                                .issuedAt(now)
                                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                                .issuer("hotel-booking-system")
                                .audience(jwt.getAudience())
                                .claim("studentId", "RUPP00")
                                .claim("scope", jwt.getClaimAsString("scope"))
                                .build();

                // Generate token
                String accessToken = accessTokenJwtEncoder
                                .encode(JwtEncoderParameters.from(jwtAccessClaimsSet))
                                .getTokenValue();

                // Get expiration of refresh token

                Instant expiresAt = jwt.getExpiresAt();
                long remainingDays = Duration.between(now, expiresAt).toDays();
                if (remainingDays < 1) {
                        // Generate JWT Refresh Token Encoder
                        JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                                        .id(auth.getName())
                                        .subject("Refresh Token")
                                        .issuer(auth.getName())
                                        .issuedAt(now)
                                        .expiresAt(now.plus(7, ChronoUnit.DAYS))
                                        .audience(List.of("NextJs", "Android", "IOS"))
                                        .claim("scope", jwt.getClaimAsString("scope"))
                                        .build();

                        refreshToken = refreshTokenEncoder
                                        .encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                                        .getTokenValue();
                }

                return AuthResponse.builder()
                                .tokenType("Bearer")
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build();
        }

        @Override
        public UserResponse findById(Integer id) {
                User user = userRepository.findById(id)
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
                return userMapper.toUserResponse(user);
        }

        @Override
        public List<UserResponse> findAll() {
                return userRepository.findAll()
                                .stream()
                                .map(userMapper::toUserResponse)
                                .toList();
        }




    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        Authentication auth = new UsernamePasswordAuthenticationToken(
                loginRequest.email(),
                loginRequest.password()
        );

        auth = daoAuthenticationProvider.authenticate(auth);

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // ✅ BLOCK UNVERIFIED USERS
        if (!user.isVerified()) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Email not verified. Please verify before login."
            );
        }

        String scope = auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();

        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject(user.getEmail())
                .issuer("hotel-booking-system")
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .audience(List.of("NextJs", "Android", "IOS"))
                .claim("role", scope)
                .claim("scope", scope)
                .build();

        JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject(user.getEmail())
                .issuer("hotel-booking-system")
                .issuedAt(now)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .audience(List.of("NextJs", "Android", "IOS"))
                .claim("role", scope)
                .claim("scope", scope)
                .build();

        String accessToken = accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();

        String refreshToken = refreshTokenEncoder
                .encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                .getTokenValue();

        return AuthResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .id(user.getId())
                .build();
    }

}
