package com.Rothana.hotel_booking_system.features.auth;

import com.Rothana.hotel_booking_system.entity.Role;
import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.features.auth.dto.*;
import com.Rothana.hotel_booking_system.features.user.RoleRepository;
import com.Rothana.hotel_booking_system.features.user.UserRepository;
import com.Rothana.hotel_booking_system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    private  final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final DaoAuthenticationProvider daoAuthenticationProvider;


    private  final JwtEncoder accessTokenJwtEncoder;
    private  final  JwtEncoder refreshTokenEncoder;

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


        return RegisterResponse.builder()
                .message("You have registered successfully!")
                .name(user.getName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }


    @Override
    public AuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        String refreshToken = refreshTokenRequest.refreshToken();
        //Authentication client with refresh Token
        Authentication auth = new BearerTokenAuthenticationToken(refreshToken);
        auth =  jwtAuthenticationProvider.authenticate(auth);


        Jwt jwt =(Jwt) auth.getPrincipal();

        //Generate JWT Token by Encoder
        //1 . Define ClaimSets(Payload)
        Instant now = Instant.now();
        JwtClaimsSet jwtAccessClaimsSet = JwtClaimsSet.builder()
                .id(jwt.getId())
                .subject(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .issuer("hotel-booking-system")
                .audience(jwt.getAudience())
                .claim("studentId", "RUPP00")
                .claim("scope",jwt.getClaimAsString("scope"))
                .build();

        //Generate token
        String accessToken = accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtAccessClaimsSet))
                .getTokenValue();

        //Get expiration of refresh token

        Instant expiresAt = jwt.getExpiresAt();
        long remainingDays = Duration.between(now, expiresAt).toDays();
        if (remainingDays < 1) {
            //Generate JWT Refresh Token Encoder
            JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                    .id(auth.getName())
                    .subject("Refresh Token")
                    .issuer(auth.getName())
                    .issuedAt(now)
                    .expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .audience(List.of("NextJs","Android","IOS"))
                    .claim("scope",jwt.getClaimAsString("scope"))
                    .build();

            refreshToken = refreshTokenEncoder
                    .encode(JwtEncoderParameters.from(jwtRefreshClaimsSet))
                    .getTokenValue();
        }

        return  AuthResponse.builder()
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
        //Authentication client with email and password
        Authentication auth  =new UsernamePasswordAuthenticationToken(loginRequest.email(),loginRequest.password());
        auth = daoAuthenticationProvider.authenticate(auth);

        //ROLE_USER ROLE_ADMIN
        String scope =auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        User user = userRepository.findByEmail(loginRequest.email())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));


        //Generate JWT Token by Encoder
        //1 . Define ClaimSets(Payload)

        Instant now = Instant.now();
        JwtClaimsSet jwtClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject(user.getEmail())                // ✅ put email here
                .issuer("hotel-booking-system")          // ✅ fixed issuer
                .issuedAt(now)
                .expiresAt(now.plus(15, ChronoUnit.MINUTES))
                .audience(List.of("NextJs","Android","IOS"))
                .claim("role", scope)
                .claim("scope", scope)
                .build();


        //Generate JWT Refresh Token Encoder
        JwtClaimsSet jwtRefreshClaimsSet = JwtClaimsSet.builder()
                .id(auth.getName())
                .subject(user.getEmail())                // ✅ email again
                .issuer("hotel-booking-system")
                .issuedAt(now)
                .claim("role", scope)
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .audience(List.of("NextJs","Android","IOS"))
                .claim("scope",scope)

                .build();


        //2 Generate token
        String accessToken =accessTokenJwtEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();

        // Generate RefreshToken
        String  refreshToken = refreshTokenEncoder
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



