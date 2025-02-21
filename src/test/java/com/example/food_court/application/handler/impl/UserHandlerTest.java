package com.example.food_court.application.handler.impl;

import com.example.food_court.application.dto.request.UserRequest;
import com.example.food_court.application.mapper.IUserRequestMapper;
import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserHandlerTest {

    @Mock
    private IUserServicePort userServicePort;

    @Mock
    private IUserRequestMapper userRequestMapper;

    @InjectMocks
    private UserHandler userHandler;

    private UserRequest userRequest;

    @BeforeEach
    public void setUp() {
        userRequest = new UserRequest();
        userRequest.setName("John");
        userRequest.setLastName("Doe");
        userRequest.setDocumentNumber("123456789");
        userRequest.setCellphoneNumber("+1234567890");
        userRequest.setDateBirth(LocalDate.of(1990, 1, 1));
        userRequest.setEmail("john.doe@example.com");
        userRequest.setPassword("password123");
    }

    @Test
    public void testSaveOwnerWithValidUserRequest() {
        User user = new User(1L, "John", "Doe", "123456789", "+1234567890", LocalDate.of(1990, 1, 1), "john.doe@example.com", "password123", null);
        when(userRequestMapper.UserRequestToUser(userRequest)).thenReturn(user);

        userHandler.saveOwner(userRequest);

        verify(userRequestMapper).UserRequestToUser(userRequest);
        verify(userServicePort).saveOwner(user);
    }
}
