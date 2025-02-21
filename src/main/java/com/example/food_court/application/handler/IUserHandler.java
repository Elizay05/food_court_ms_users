package com.example.food_court.application.handler;

import com.example.food_court.application.dto.request.UserRequest;

public interface IUserHandler {
    void saveOwner(UserRequest userRequest);
    boolean isOwner (String documentNumber);
}
