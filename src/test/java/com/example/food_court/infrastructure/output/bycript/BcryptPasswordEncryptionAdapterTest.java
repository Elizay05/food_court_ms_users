package com.example.food_court.infrastructure.output.bycript;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class BcryptPasswordEncryptionAdapterTest {

    @Test
    public void test_password_is_successfully_encrypted() {
        BcryptPasswordEncryptionAdapter encryptionAdapter = new BcryptPasswordEncryptionAdapter();

        String password = "myPassword123";
        String encryptedPassword = encryptionAdapter.encryptPassword(password);

        assertNotNull(encryptedPassword);
        assertNotEquals(password, encryptedPassword);
        assertTrue(BCrypt.checkpw(password, encryptedPassword));
    }

    @Test
    public void test_empty_password_encryption() {
        BcryptPasswordEncryptionAdapter encryptionAdapter = new BcryptPasswordEncryptionAdapter();

        String emptyPassword = "";
        String encryptedPassword = encryptionAdapter.encryptPassword(emptyPassword);

        assertNotNull(encryptedPassword);
        assertNotEquals(emptyPassword, encryptedPassword);
        assertTrue(BCrypt.checkpw(emptyPassword, encryptedPassword));
    }
}
