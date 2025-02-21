package com.example.food_court.domain.spi;

public interface IPasswordEncryptionPort {
    String encryptPassword(String password);
}
