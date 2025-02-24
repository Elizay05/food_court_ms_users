package com.example.food_court.application.handler.impl;

import com.example.food_court.application.dto.request.AuthenticationRequest;
import com.example.food_court.application.mapper.IAuthenticationRequestMapper;
import com.example.food_court.domain.api.IAuthenticationServicePort;
import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationHandlerTest {

    @Mock
    IAuthenticationServicePort authenticationServicePort;

    @Mock
    IAuthenticationRequestMapper authenticationRequestMapper;

    @InjectMocks
    private AuthenticationHandler authenticationHandler;

    @Test
    public void test_authenticate_returns_valid_token() {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "password123");

        Authentication authentication = new Authentication("test@email.com", "password123");
        Token expectedToken = new Token("validToken");

        when(authenticationRequestMapper.ToAuthentication(request)).thenReturn(authentication);
        when(authenticationServicePort.authenticate(authentication)).thenReturn(expectedToken);

        // Act
        Token result = authenticationHandler.authenticate(request);

        // Assert
        assertEquals(expectedToken.getToken(), result.getToken());
        verify(authenticationRequestMapper).ToAuthentication(request);
        verify(authenticationServicePort).authenticate(authentication);
    }
}
