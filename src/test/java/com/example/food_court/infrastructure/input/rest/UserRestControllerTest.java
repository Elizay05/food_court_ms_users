package com.example.food_court.infrastructure.input.rest;

import com.example.food_court.application.dto.request.UserRequest;
import com.example.food_court.application.handler.impl.UserHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@SpringBootTest
public class UserRestControllerTest {
    @Mock
    private UserHandler userHandler;

    @InjectMocks
    private UserRestController userRestController;

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
    public void testSaveOwner() {
        doNothing().when(userHandler).saveOwner(userRequest);

        ResponseEntity<Void> response = userRestController.saveOwner(userRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userHandler).saveOwner(userRequest);
    }

    @Test
    public void testValidateOwnerReturnsTrueForValidDocument() {
        String validDocumentNumber = "123456789";
        UserRestController userRestController = new UserRestController(userHandler);

        when(userHandler.isOwner(validDocumentNumber)).thenReturn(true);

        ResponseEntity<Boolean> response = userRestController.validateOwner(validDocumentNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody());
        verify(userHandler).isOwner(validDocumentNumber);
    }
}
