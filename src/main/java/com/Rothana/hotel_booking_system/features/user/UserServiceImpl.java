package com.Rothana.hotel_booking_system.features.user;

import com.Rothana.hotel_booking_system.entity.User;
import com.Rothana.hotel_booking_system.features.user.dto.UpdateUserRequest;

import com.Rothana.hotel_booking_system.features.auth.dto.UserResponse;
import com.Rothana.hotel_booking_system.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse update(Integer id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        // check email uniqueness (if email is changed)
        if (!user.getEmail().equals(request.email()) && userRepository.existsAllByEmail(request.email())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already in use");
        }

        user.setName(request.name());
        user.setEmail(request.email());
        user.setPhoneNumber(request.phoneNumber());

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }


    @Override
    @Transactional
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}