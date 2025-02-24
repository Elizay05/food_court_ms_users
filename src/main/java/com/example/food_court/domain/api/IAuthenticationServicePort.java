package com.example.food_court.domain.api;

import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;

public interface IAuthenticationServicePort {

    Token authenticate(Authentication authentication);
}
