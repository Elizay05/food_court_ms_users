package com.example.food_court.domain.spi;

import com.example.food_court.domain.model.User;

public interface IUserPersistencePort {
    User saveOwner(User user);
    boolean isOwner(String documentNumber);
    User saveEmployee (User user);
    void updateNit (String documentNumber, String nitRestaurant);
    User saveCustomer (User user);
}
