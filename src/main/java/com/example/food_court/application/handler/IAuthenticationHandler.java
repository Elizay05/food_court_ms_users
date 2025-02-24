package com.example.food_court.application.handler;

import com.example.food_court.application.dto.request.AuthenticationRequest;
import com.example.food_court.domain.model.Token;

public interface IAuthenticationHandler {
    Token authenticate(AuthenticationRequest authenticationRequest);
}
