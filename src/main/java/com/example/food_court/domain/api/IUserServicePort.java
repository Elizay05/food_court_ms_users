package com.example.food_court.domain.api;

import com.example.food_court.domain.model.User;

public interface IUserServicePort {
    User saveOwner(User user);
}
