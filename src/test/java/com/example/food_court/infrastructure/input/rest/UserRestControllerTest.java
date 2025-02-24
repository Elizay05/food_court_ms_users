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
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
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

    @Test
    public void test_save_employee_success() {
        UserRequest userRequest = new UserRequest();
        userRequest.setName("John");
        userRequest.setLastName("Doe");
        userRequest.setDocumentNumber("12345");
        userRequest.setCellphoneNumber("+573005698325");
        userRequest.setDateBirth(LocalDate.of(1990, 1, 1));
        userRequest.setEmail("john@example.com");
        userRequest.setPassword("password123");

        doNothing().when(userHandler).saveEmployee(any(UserRequest.class));

        // Act
        ResponseEntity<Void> response = userRestController.saveEmployee(userRequest);

        // Assert
        verify(userHandler).saveEmployee(userRequest);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    public void test_admin_can_update_nit() {
        String documentNumber = "123456";
        String nitRestaurant = "987654";

        doNothing().when(userHandler).updateNit(documentNumber, nitRestaurant);

        // Act
        ResponseEntity<String> response = userRestController.updateNit(documentNumber, nitRestaurant);

        // Assert
        verify(userHandler).updateNit(documentNumber, nitRestaurant);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
