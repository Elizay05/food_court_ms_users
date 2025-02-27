package com.example.food_court.domain.spi;

import com.example.food_court.domain.model.User;

import java.util.Optional;

public interface IUserPersistencePort {
    User saveOwner(User user);
    boolean isOwner(String documentNumber);
    User saveEmployee (User user);
    void updateNit (String documentNumber, String nitRestaurant);
    User saveCustomer (User user);
    Optional<User> getUserByDocument(String documentNumber);
}
