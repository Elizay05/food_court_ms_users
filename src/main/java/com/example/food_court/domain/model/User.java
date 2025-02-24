package com.example.food_court.domain.model;

import java.time.LocalDate;

public class User {

    private final Long id;
    private final String name;
    private final String lastName;
    private final String documentNumber;
    private final String cellphoneNumber;
    private final LocalDate dateBirth;
    private final String email;
    private String password;
    private Role role;
    private String nit;

    public User(Long id, String name, String lastName, String documentNumber, String cellphoneNumber, LocalDate dateBirth, String email, String password, Role role, String nit) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.documentNumber = documentNumber;
        this.cellphoneNumber = cellphoneNumber;
        this.dateBirth = dateBirth;
        this.email = email;
        this.password = password;
        this.role = role;
        this.nit = nit;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public LocalDate getDateBirth() {
        return dateBirth;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getNit() {return nit;}

    public void setNit(String nit) {this.nit = nit;}

}
