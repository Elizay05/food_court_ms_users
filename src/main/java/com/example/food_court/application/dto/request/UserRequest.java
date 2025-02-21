package com.example.food_court.application.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Document number is required")
    @Pattern(regexp = "\\d+", message = "Document number must contain only digits")
    private String documentNumber;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+?\\d{1,13}$", message = "Invalid phone number format. Example: +573005698325")
    private String cellphoneNumber;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateBirth;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 3, message = "Password must be at least 6 characters long")
    private String password;
}
