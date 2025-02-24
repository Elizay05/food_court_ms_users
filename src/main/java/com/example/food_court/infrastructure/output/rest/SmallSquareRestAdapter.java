package com.example.food_court.infrastructure.output.rest;

import com.example.food_court.domain.spi.ISmallSquarePersistencePort;
import com.example.food_court.domain.util.DomainConstants;
import com.example.food_court.infrastructure.exception.ElementNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class SmallSquareRestAdapter implements ISmallSquarePersistencePort {

    private final RestTemplate restTemplate;
    private final HttpServletRequest request;

    @Override
    public String validateNit() {
        String token = request.getHeader("Authorization");

        if (token == null || token.isEmpty()) {
            throw new RuntimeException("No se encontró un token de autenticación válido");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    DomainConstants.URL_VALIDATE_NIT,
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new ElementNotFoundException("No se encontró un restaurante asociado a el propietario que crea el empleado.");
            }
            throw new RuntimeException("Error al obtener el NIT del microservicio de restaurantes: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al obtener el NIT del microservicio de restaurantes: " + e.getMessage());
        }
    }
}
