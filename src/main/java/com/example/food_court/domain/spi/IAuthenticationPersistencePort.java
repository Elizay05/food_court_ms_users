package com.example.food_court.domain.spi;

import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;

public interface IAuthenticationPersistencePort {

    Token authenticate(Authentication authentication);
}
