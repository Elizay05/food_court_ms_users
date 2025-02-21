package com.example.food_court.infrastructure.output.jpa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name field cannot be empty")
    private String name;

    @NotBlank(message = "Last name field cannot be empty")
    private String lastName;

    @NotBlank(message = "Identification document field cannot be empty")
    private String documentNumber;

    @NotBlank(message = "Cellphone number field cannot be empty")
    private String cellphoneNumber;

    @NotBlank(message = "Date of birth field cannot be empty")
    private String dateBirth;

    @NotBlank(message = "Email field cannot be empty")
    private String email;

    @NotBlank(message = "Password field cannot be empty")
    private String password;

    @ManyToOne
    @JoinColumn(name = "id_rol")
    private RoleEntity role;
}
