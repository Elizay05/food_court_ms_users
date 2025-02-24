package com.example.food_court.infrastructure.output.jpa.adapter;

import com.example.food_court.infrastructure.configuration.security.CustomUserDetails;
import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserDetailsServiceImplTest {
    @Mock
    private IUserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Test
    public void test_load_user_by_username_returns_custom_user_details_when_user_exists() {
        // Arrange
        String email = "test@email.com";
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(email);
        userEntity.setPassword("password");

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(userEntity));

        // Act
        UserDetails result = userDetailsServiceImpl.loadUserByUsername(email);

        // Assert
        assertNotNull(result);
        assertTrue(result instanceof CustomUserDetails);
        assertEquals(email, result.getUsername());
    }

    @Test
    public void test_load_user_by_username_throws_exception_when_user_not_found() {
        // Arrange
        String email = "nonexistent@email.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsServiceImpl.loadUserByUsername(email);
        });
    }
}
