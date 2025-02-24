package com.example.food_court.infrastructure.input.rest;

import com.example.food_court.application.dto.request.AuthenticationRequest;
import com.example.food_court.application.dto.response.TokenResponse;
import com.example.food_court.application.handler.impl.AuthenticationHandler;
import com.example.food_court.application.mapper.ITokenResponseMapper;
import com.example.food_court.domain.model.Token;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthenticationRestControllerTest {
    @Mock
    private AuthenticationHandler authenticationHandler;

    @Mock
    private ITokenResponseMapper tokenResponseMapper;


    @InjectMocks
    private AuthenticationRestController authenticationRestController;

    @Test
    public void test_valid_login_returns_ok_with_token() {
        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "password");
        Token token = new Token("jwt-token");
        TokenResponse tokenResponse = new TokenResponse("jwt-token");

        when(authenticationHandler.authenticate(request)).thenReturn(token);
        when(tokenResponseMapper.toTokenResponse(token)).thenReturn(tokenResponse);

        ResponseEntity<TokenResponse> response = authenticationRestController.login(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("jwt-token", response.getBody().getToken());
        verify(authenticationHandler).authenticate(request);
        verify(tokenResponseMapper).toTokenResponse(token);
    }
}
