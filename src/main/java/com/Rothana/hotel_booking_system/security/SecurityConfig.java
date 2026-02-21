package com.Rothana.hotel_booking_system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final CookieBearerTokenResolver cookieBearerTokenResolver;

    @Bean
    JwtAuthenticationProvider configJwtAuthenticationProvider(@Qualifier("refreshTokenJwtDecoder") JwtDecoder refreshTokenJwtDecoder) {
        JwtAuthenticationProvider auth = new JwtAuthenticationProvider(refreshTokenJwtDecoder);
        return auth;
    }

    @Bean
    DaoAuthenticationProvider configDaoAuthenticationProvider() {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }
    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        // Tell Spring to look at the "scope" claim you defined in AuthServiceImpl
        grantedAuthoritiesConverter.setAuthoritiesClaimName("scope");

        // Map "ADMIN" to "ROLE_ADMIN" so hasAnyRole("ADMIN") works
        grantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }

    @Bean
    SecurityFilterChain configureApiSecurity(HttpSecurity http, @Qualifier("accessTokenjwtDecoder") JwtDecoder jwtDecoder) throws Exception {

        // Endpoint Security config
        http.authorizeHttpRequests(endpoint -> endpoint
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/upload/**").permitAll()

                .requestMatchers("/api/v1/telegram/test/**").permitAll()

                .requestMatchers("/api/v1/payments/paypal/**").permitAll()
                .requestMatchers("/api/v1/maintenance-tickets/**").permitAll()
                .requestMatchers("/api/v1/housekeeping-tasks/**").permitAll()
                .requestMatchers("/api/v1/room-calendar/**").permitAll()

                .requestMatchers(HttpMethod.GET,  "/api/v1/inventory/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/inventory/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/v1/inventory/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/inventory/**").hasAnyRole("ADMIN")

                .requestMatchers(HttpMethod.GET,  "/api/v1/booking_services/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/booking_services/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/v1/booking_services/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/booking_services/**").hasAnyRole("ADMIN")

                .requestMatchers(HttpMethod.GET,  "/api/v1/booking/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/booking/**").permitAll()
                // ⬇️ ADD THIS LINE so create() is allowed for logged-in users
                .requestMatchers(HttpMethod.POST, "/api/v1/bookings/**").hasAnyRole("ADMIN", "STAFF", "USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/bookings/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/bookings/**").hasAnyRole("ADMIN")


                .requestMatchers(HttpMethod.GET,  "/api/v1/services/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/services/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.PUT, "/api/v1/services/**").hasAnyRole("ADMIN", "STAFF")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/services/**").hasAnyRole("ADMIN")

                 .requestMatchers(HttpMethod.GET, "/api/v1/rooms/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/rooms/**").hasAnyRole("ADMIN", "STAFF","USER")
                .requestMatchers(HttpMethod.PUT, "/api/v1/rooms/**").hasAnyRole("ADMIN", "STAFF","USER")
                .requestMatchers(HttpMethod.DELETE, "/api/v1/rooms/**").hasAnyRole("ADMIN")

                .requestMatchers(HttpMethod.GET, "/api/v1/roomTypes/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/api/v1/roomTypes/**").hasAnyRole("ADMIN","STAFF")
                .requestMatchers(HttpMethod.PUT,  "/api/v1/roomTypes/**").hasAnyRole("ADMIN","STAFF")
                .requestMatchers(HttpMethod.DELETE,"/api/v1/roomTypes/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/api/v1/upload/**").hasAnyRole("ADMIN","STAFF")

                .anyRequest().authenticated());

        // Security Mechanism (JWT) - only for authenticated endpoints
//        http.oauth2ResourceServer(oauth2 -> oauth2
//                .jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder)));
        http.oauth2ResourceServer(oauth2 -> oauth2
                .bearerTokenResolver(cookieBearerTokenResolver)
                .jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(jwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter())
                )
        );

        // Disable CSRF Token (Cross Site Request Forgery)
        http.csrf(csrf -> csrf.disable());

        // Make Stateless Session
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}