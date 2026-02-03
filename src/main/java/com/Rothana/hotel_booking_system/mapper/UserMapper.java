package com.Rothana.hotel_booking_system.mapper;

import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.features.auth.dto.AuthResponse;
import com.Rothana.hotel_booking_system.features.auth.dto.LoginRequest;
import com.Rothana.hotel_booking_system.features.auth.dto.RegisterRequest;
import com.Rothana.hotel_booking_system.features.auth.dto.UserResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromRegisterRequest(RegisterRequest registerRequest);
    User fromLoginRequest(LoginRequest loginRequest);
    UserResponse toUserResponse(User user);

}
