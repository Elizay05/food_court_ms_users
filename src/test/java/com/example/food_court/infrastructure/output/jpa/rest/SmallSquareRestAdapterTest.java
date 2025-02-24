package com.example.food_court.infrastructure.output.jpa.rest;

import com.example.food_court.domain.util.DomainConstants;
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
    public void test_validate_nit_returns_response_body_when_valid_token() {
        // Arrange
        RestTemplate restTemplate = mock(RestTemplate.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        SmallSquareRestAdapter adapter = new SmallSquareRestAdapter(restTemplate, request);

        String token = "valid-token";
        String expectedResponse = "response-body";

        when(request.getHeader("Authorization")).thenReturn(token);

        ResponseEntity<String> responseEntity = new ResponseEntity<>(expectedResponse, HttpStatus.OK);
        when(restTemplate.exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        )).thenReturn(responseEntity);

        // Act
        String result = adapter.validateNit();

        // Assert
        assertEquals(expectedResponse, result);
        verify(request).getHeader("Authorization");
        verify(restTemplate).exchange(
                eq(DomainConstants.URL_VALIDATE_NIT),
                eq(HttpMethod.GET),
                any(HttpEntity.class),
                eq(String.class)
        );
    }
    @Test
    public void test_validate_nit_throws_exception_when_token_null() {
        // Arrange
        RestTemplate restTemplate = mock(RestTemplate.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        SmallSquareRestAdapter adapter = new SmallSquareRestAdapter(restTemplate, request);

        when(request.getHeader("Authorization")).thenReturn(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            adapter.validateNit();
        });

        assertEquals("No se encontró un token de autenticación válido", exception.getMessage());
        verify(request).getHeader("Authorization");
        verifyNoInteractions(restTemplate);
    }
}
