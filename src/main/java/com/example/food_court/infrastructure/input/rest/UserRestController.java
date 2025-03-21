package com.example.food_court.infrastructure.input.rest;

import com.example.food_court.application.dto.request.UserRequest;
import com.example.food_court.application.handler.impl.UserHandler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User-related operations")
public class UserRestController {

    private final UserHandler userHandler;

    @Operation(
            summary = "Create a new owner",
            description = "Registers a new owner in the system."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details for registration",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRequest.class)
            )
    )
    @ApiResponse(responseCode = "201", description = "Owner successfully created")
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json"))
    @PostMapping("/saveOwner")
    @PreAuthorize("hasRole('ROLE_Administrator')")
    public ResponseEntity<Void> saveOwner(@Valid @RequestBody UserRequest userRequest) {
        userHandler.saveOwner(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Validate Owner",
            description = "Checks if a given document number belongs to a valid owner."
    )
    @ApiResponse(responseCode = "200", description = "Returns true if the owner is valid, false otherwise",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Boolean.class)))
    @ApiResponse(responseCode = "400", description = "Invalid document number format",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    @PreAuthorize("hasRole('ROLE_Administrator')")
    @GetMapping("/validate-owner/{documentNumber}")
    public ResponseEntity<Boolean> validateOwner(@PathVariable String documentNumber) {
        boolean isValid = userHandler.isOwner(documentNumber);
        return ResponseEntity.ok(isValid);
    }

    @Operation(
            summary = "Create a new employee",
            description = "Registers a new employee in the system."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details for registration",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRequest.class)
            )
    )
    @ApiResponse(responseCode = "201", description = "Employee successfully created")
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json"))
    @PostMapping("/saveEmployee")
    @PreAuthorize("hasRole('ROLE_Owner')")
    public ResponseEntity<Void> saveEmployee(@Valid @RequestBody UserRequest userRequest) {
        userHandler.saveEmployee(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(
            summary = "Update restaurant NIT for an owner",
            description = "Updates the NIT (tax identification number) of a restaurant associated with a given owner document number."
    )
    @ApiResponse(responseCode = "200", description = "NIT successfully updated")
    @ApiResponse(responseCode = "400", description = "Invalid document number or NIT format",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "403", description = "Forbidden - User lacks necessary permissions",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Owner not found",
            content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "500", description = "Internal server error",
            content = @Content(mediaType = "application/json"))
    @PutMapping("/updateNit/{documentNumber}/{nitRestaurant}")
    @PreAuthorize("hasRole('ROLE_Administrator')")
    public ResponseEntity<String> updateNit(
            @PathVariable String documentNumber,
            @PathVariable String nitRestaurant) {
        userHandler.updateNit(documentNumber, nitRestaurant);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(
            summary = "Create a new customer",
            description = "Registers a new customer in the system."
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "User details for registration",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UserRequest.class)
            )
    )
    @ApiResponse(responseCode = "201", description = "Customer successfully created")
    @ApiResponse(responseCode = "400", description = "Validation error",
            content = @Content(mediaType = "application/json"))
    @PostMapping("/saveCustomer")
    public ResponseEntity<Void> saveCustomer(@Valid @RequestBody UserRequest userRequest) {
        userHandler.saveCustomer(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/getPhoneByDocument/{documentNumber}")
    @PreAuthorize("hasRole('ROLE_Employee')")
    public ResponseEntity<String> getPhoneByDocument(@PathVariable String documentNumber) {
        String phoneNumber = userHandler.getPhoneByDocument(documentNumber);
        return ResponseEntity.ok(phoneNumber);
    }
}
