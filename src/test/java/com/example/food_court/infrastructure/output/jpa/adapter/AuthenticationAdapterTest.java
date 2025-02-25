package com.example.food_court.infrastructure.output.jpa.adapter;

import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;
import com.example.food_court.infrastructure.configuration.security.jwt.JwtService;
import com.example.food_court.infrastructure.exception.IncorrectCredentialsException;
import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AuthenticationAdapterTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private AuthenticationJpaAdapter authenticationAdapter;

    @Test
    public void test_authenticate_success_with_valid_credentials() {
        // Arrange
        String email = "test@email.com";
        String password = "password123";
        String documentNumber = "12345";
        String nit = "1127532669";
        String jwtToken = "generated.jwt.token";

        Authentication authentication = new Authentication(email, password);
        UserDetails userDetails = mock(UserDetails.class);
        UserEntity userEntity = UserEntity.builder()
                .email(email)
                .documentNumber(documentNumber)
                .nit(nit)
                .build();

        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userDetailsService.loadUserByUsername(email)).thenReturn(userDetails);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));
        when(jwtService.generateToken(userDetails, documentNumber, nit)).thenReturn(jwtToken);

        // Act
        Token result = authenticationAdapter.authenticate(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(jwtToken, result.getToken());
        verify(authenticationManager).authenticate(any());
        verify(userDetailsService).loadUserByUsername(email);
        verify(userRepository).findByEmail(email);
        verify(jwtService).generateToken(userDetails, documentNumber, nit);
    }

    @Test
    public void test_authenticate_fails_with_incorrect_password() {
        // Arrange
        String email = "test@email.com";
        String wrongPassword = "wrongpass";
        Authentication authentication = new Authentication(email, wrongPassword);

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        // Act & Assert
        assertThrows(IncorrectCredentialsException.class, () -> {
            authenticationAdapter.authenticate(authentication);
        });

        verify(authenticationManager).authenticate(any());
        verifyNoInteractions(userDetailsService);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(jwtService);
    }
}
