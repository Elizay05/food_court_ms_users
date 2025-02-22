package com.example.food_court.application.handler.impl;

import com.example.food_court.application.dto.request.AuthenticationRequest;
import com.example.food_court.application.handler.IAuthenticationHandler;
import com.example.food_court.application.mapper.IAuthenticationRequestMapper;
import com.example.food_court.domain.api.IAuthenticationServicePort;
import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationHandler implements IAuthenticationHandler {

    private final IAuthenticationServicePort authenticationServicePort;
    private final IAuthenticationRequestMapper authenticationRequestMapper;

    @Override
    public Token authenticate(@Valid AuthenticationRequest authenticationRequest) {
        Authentication authentication = authenticationRequestMapper.ToAuthentication(authenticationRequest);
        return authenticationServicePort.authenticate(authentication);
    }
}
