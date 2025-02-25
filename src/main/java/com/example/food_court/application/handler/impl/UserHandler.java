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

    @Override
    public boolean isOwner(String documentNumber) {
        return userServicePort.isOwner(documentNumber);
    }

    @Override
    public void saveEmployee(@Valid UserRequest userRequest) {
        User user = userRequestMapper.UserRequestToUser(userRequest);
        userServicePort.saveEmployee(user);
    }

    @Override
    public void updateNit(String documentNumber, String nitRestaurant){
        userServicePort.updateNit(documentNumber, nitRestaurant);
    }

    @Override
    public void saveCustomer(@Valid UserRequest userRequest){
        User user = userRequestMapper.UserRequestToUser(userRequest);
        userServicePort.saveCustomer(user);
    }
}
