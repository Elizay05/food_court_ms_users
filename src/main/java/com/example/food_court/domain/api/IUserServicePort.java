package com.example.food_court.domain.api;

import com.example.food_court.domain.model.User;

public interface IUserServicePort {
    User saveOwner(User user);
    boolean isOwner(String documentNumber);
    User saveEmployee(User user);
    void updateNit (String documentNumber, String nitRestaurant);
    User saveCustomer (User user);
}
