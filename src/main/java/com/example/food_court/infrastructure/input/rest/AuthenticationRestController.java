package com.example.food_court.infrastructure.input.rest;

import com.example.food_court.application.dto.request.AuthenticationRequest;
import com.example.food_court.application.dto.response.TokenResponse;
import com.example.food_court.application.handler.impl.AuthenticationHandler;
import com.example.food_court.application.mapper.ITokenResponseMapper;
import com.example.food_court.domain.model.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationRestController {

    private final AuthenticationHandler authenticationHandler;
    private final ITokenResponseMapper tokenResponseMapper;

    @Operation(
            summary = "User Login",
            description = "Authenticates a user and returns a JWT token."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User credentials for authentication",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AuthenticationRequest.class)
            )
    )
    @ApiResponse(responseCode = "200", description = "Successful authentication",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TokenResponse.class)))
    @ApiResponse(responseCode = "400", description = "Invalid credentials or request format",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "401", description = "Unauthorized - Invalid username or password",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login (@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        Token token = authenticationHandler.authenticate(authenticationRequest);
        TokenResponse response = tokenResponseMapper.toTokenResponse(token);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}