package com.example.food_court.infrastructure.output.bycript;

import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordEncryptionAdapter implements IPasswordEncryptionPort {
    @Override
    public String encryptPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
