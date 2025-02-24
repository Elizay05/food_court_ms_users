package com.example.food_court.infrastructure.exceptionhandler;

import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.infrastructure.exception.ElementNotFoundException;
import com.example.food_court.infrastructure.exception.FieldAlreadyExistsException;
import com.example.food_court.infrastructure.exception.IncorrectCredentialsException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class GlobalExceptionHandlerTest {

    @Test
    public void testHandleValidationxExceptionReturnsErrorMap() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "field1", "error message"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("error message", response.getBody().get("field1"));
    }

    @Test
    public void testHandleMultipleValidationErrors() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        BindingResult bindingResult = new BeanPropertyBindingResult(new Object(), "object");
        bindingResult.addError(new FieldError("object", "field1", "error1"));
        bindingResult.addError(new FieldError("object", "field2", "error2"));
        bindingResult.addError(new FieldError("object", "field3", "error3"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        ResponseEntity<Map<String, String>> response = handler.handleValidationExceptions(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(3, response.getBody().size());
        assertEquals("error1", response.getBody().get("field1"));
        assertEquals("error2", response.getBody().get("field2"));
        assertEquals("error3", response.getBody().get("field3"));
    }

    @Test
    public void testHandleElementNotFoundExceptionReturnsNotFoundStatus() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        ElementNotFoundException ex = new ElementNotFoundException("Element not found");

        ResponseEntity<ExceptionResponse> response = handler.handleElementNotFoundException(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Element not found", response.getBody().getMessage());
        assertEquals(HttpStatus.NOT_FOUND.toString(), response.getBody().getStatus());
    }

    @Test
    public void testHandleFieldAlreadyExistsExceptionReturnsExceptionResponseWithBadRequestStatus() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        String errorMessage = "Field already exists";
        FieldAlreadyExistsException ex = new FieldAlreadyExistsException(errorMessage);

        ResponseEntity<ExceptionResponse> response = handler.handleFieldAlreadyExistsException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getBody().getStatus());
    }

    @Test
    public void testHandleGeneralExceptionReturnsInternalServerError() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        Exception ex = new Exception("Unexpected error occurred");

        ResponseEntity<String> response = handler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Error inesperado: Unexpected error occurred", response.getBody());
    }

    @Test
    public void testHandleInvalidArgumentsExceptionReturnsExceptionResponseWithBadRequestStatus() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        InvalidArgumentsException ex = new InvalidArgumentsException("Invalid arguments provided");

        ResponseEntity<ExceptionResponse> response = handler.handleInvalidArgumentsException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid arguments provided", response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getBody().getStatus());
        assertNotNull(response.getBody().getTimestamp());
    }

    @Test
    public void testHandleIncorrectCredentialsExceptionReturnsExceptionResponseWithBadRequestStatus() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();

        String errorMessage = "Incorect credentials";
        IncorrectCredentialsException ex = new IncorrectCredentialsException(errorMessage);

        ResponseEntity<ExceptionResponse> response = handler.handleIncorrectCredentialsException(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals(HttpStatus.BAD_REQUEST.toString(), response.getBody().getStatus());
    }
}
