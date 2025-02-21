package com.example.food_court.application.handler.impl;

import com.example.food_court.application.dto.request.UserRequest;
import com.example.food_court.application.handler.IUserHandler;
import com.example.food_court.application.mapper.IUserRequestMapper;
import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.model.User;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserHandler implements IUserHandler {

    private final IUserServicePort userServicePort;
    private final IUserRequestMapper userRequestMapper;

    @Override
    public void saveOwner(@Valid UserRequest userRequest) {
        User user = userRequestMapper.UserRequestToUser(userRequest);
        userServicePort.saveOwner(user);
    }
}
