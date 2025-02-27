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
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

    @Test
    public void test_save_customer_returns_created_status() {
        doNothing().when(userHandler).saveCustomer(any(UserRequest.class));

        // Act
        ResponseEntity<Void> response = userRestController.saveCustomer(userRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(userHandler).saveCustomer(userRequest);
    }

    @Test
    public void test_get_phone_by_document_success() {
        String documentNumber = "123456789";
        String expectedPhone = "1234567890";

        when(userHandler.getPhoneByDocument(documentNumber)).thenReturn(expectedPhone);

        ResponseEntity<String> response = userRestController.getPhoneByDocument(documentNumber);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPhone, response.getBody());
        verify(userHandler).getPhoneByDocument(documentNumber);
    }

    @Test
    public void test_get_phone_by_document_not_found() {
        String nonExistentDoc = "999999999";

        when(userHandler.getPhoneByDocument(nonExistentDoc))
                .thenThrow(new NoSuchElementException("User not found"));

        assertThrows(NoSuchElementException.class, () -> {
            userRestController.getPhoneByDocument(nonExistentDoc);
        });

        verify(userHandler).getPhoneByDocument(nonExistentDoc);
    }
}
