package com.example.food_court.domain.usecase;

import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;
import com.example.food_court.domain.spi.IAuthenticationPersistencePort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationUseCaseTest {

    @Mock
    private IAuthenticationPersistencePort authenticationPersistencePort;

    @InjectMocks
    private AuthenticationUseCase authenticationUseCase;

    @Test
    public void test_valid_email_authenticates_successfully() {
        // Arrange
        Authentication auth = new Authentication("test@email.com", "password123");
        Token expectedToken = new Token("token123");
        when(authenticationPersistencePort.authenticate(auth)).thenReturn(expectedToken);

        // Act
        Token result = authenticationUseCase.authenticate(auth);

        // Assert
        assertEquals(expectedToken.getToken(), result.getToken());
        verify(authenticationPersistencePort).authenticate(auth);
    }

    @Test
    public void test_invalid_email_throws_exception() {
        // Arrange
        Authentication auth = new Authentication("invalid.email", "password123");

        // Act & Assert
        InvalidArgumentsException exception = assertThrows(
                InvalidArgumentsException.class,
                () -> authenticationUseCase.authenticate(auth)
        );
        assertEquals("The email is not valid.", exception.getMessage());
        verify(authenticationPersistencePort, never()).authenticate(any());
    }
}
