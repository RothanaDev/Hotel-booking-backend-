package com.Rothana.hotel_booking_system.features.user;

import com.Rothana.hotel_booking_system.features.user.dto.UpdateUserRequest;
import com.Rothana.hotel_booking_system.features.auth.dto.UserResponse;

public interface UserService {
    UserResponse update(Integer id, UpdateUserRequest request);
    void delete(Integer id);
}
