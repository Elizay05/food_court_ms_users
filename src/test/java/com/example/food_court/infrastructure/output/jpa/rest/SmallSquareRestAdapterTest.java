package com.example.food_court.infrastructure.output.jpa.rest;

import com.example.food_court.domain.util.DomainConstants;
import com.example.food_court.infrastructure.exception.ElementNotFoundException;
import com.example.food_court.infrastructure.output.rest.SmallSquareRestAdapter;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class SmallSquareRestAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private HttpServletRequest httpServletRequest;

    @InjectMocks
    private SmallSquareRestAdapter smallSquareRestAdapter;

    @Test
    public void test_valid_token_returns_nit_validation() {
        String token = "Bearer valid-token";
        String expectedResponse = "123456789";

        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        // Act
        String result = smallSquareRestAdapter.validateNit();

        // Assert
        assertEquals(expectedResponse, result);
        verify(httpServletRequest).getHeader("Authorization");
        verify(restTemplate).exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    public void test_missing_token_throws_exception() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            smallSquareRestAdapter.validateNit();
        });

        assertEquals("No se encontró un token de autenticación válido", exception.getMessage());
        verify(httpServletRequest).getHeader("Authorization");
        verifyNoInteractions(restTemplate);
    }

    @Test
    public void test_empty_authorization_token_throws_runtime_exception() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            smallSquareRestAdapter.validateNit();
        });

        verify(httpServletRequest).getHeader("Authorization");
    }

    @Test
    public void test_http_404_throws_element_not_found_exception() {
        String token = "Bearer valid-token";

        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        when(restTemplate.exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(ElementNotFoundException.class, () -> {
            smallSquareRestAdapter.validateNit();
        });

        verify(httpServletRequest).getHeader("Authorization");
        verify(restTemplate).exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    public void test_other_http_client_errors_throw_runtime_exception() {
        String token = "Bearer valid-token";

        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        when(restTemplate.exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Bad Request"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            smallSquareRestAdapter.validateNit();
        });

        assertEquals("Error al obtener el NIT del microservicio de restaurantes: 400 Bad Request", exception.getMessage());
        verify(httpServletRequest).getHeader("Authorization");
        verify(restTemplate).exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
    }

    @Test
    public void test_generic_exception_throws_runtime_exception() {
        String token = "Bearer valid-token";

        when(httpServletRequest.getHeader("Authorization")).thenReturn(token);

        when(restTemplate.exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenThrow(new RuntimeException("Unexpected error"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            smallSquareRestAdapter.validateNit();
        });

        assertEquals("Error inesperado al obtener el NIT del microservicio de restaurantes: Unexpected error", exception.getMessage());
        verify(httpServletRequest).getHeader("Authorization");
        verify(restTemplate).exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
    }
}
