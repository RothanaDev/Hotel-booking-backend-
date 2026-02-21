package com.Rothana.hotel_booking_system.security;

import com.Rothana.hotel_booking_system.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class CustomUserDetail implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return user.getRoles(); // Role must implement GrantedAuthority
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // must be BCrypt in DB
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // login by email
    }

    // ✅ IMPORTANT: return true or login will fail
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}