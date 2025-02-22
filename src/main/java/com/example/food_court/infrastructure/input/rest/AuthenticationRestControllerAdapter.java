package com.example.food_court.infrastructure.input.rest;

import com.example.food_court.application.dto.request.AuthenticationRequest;
import com.example.food_court.application.dto.response.TokenResponse;
import com.example.food_court.application.handler.impl.AuthenticationHandler;
import com.example.food_court.application.mapper.ITokenResponseMapper;
import com.example.food_court.domain.model.Token;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestControllerAdapter {

    private final AuthenticationHandler authenticationHandler;
    private final ITokenResponseMapper tokenResponseMapper;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login (@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        Token token = authenticationHandler.authenticate(authenticationRequest);
        TokenResponse response = tokenResponseMapper.toTokenResponse(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
