package com.example.food_court.domain.usecase;

import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.model.Role;
import com.example.food_court.domain.model.User;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages.INVALID_ARGUMENTS_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncryptionPort passwordEncryptionPort;

    @Test
    public void SaveOwnerWithValidAdultUserReturnsSavedUser() {
        UserCase userCase = new UserCase(userPersistencePort, passwordEncryptionPort);

        User adultUser = new User(1L, "John", "Doe", "123", "+1234567890",
                LocalDate.now().minusYears(20), "john@email.com", "password", new Role(2L, "OWNER", "description"));
        User expectedUser = new User(1L, "John", "Doe", "123", "+1234567890",
                LocalDate.now().minusYears(20), "john@email.com", "encrypted", new Role(2L, "OWNER", "description"));

        when(passwordEncryptionPort.encryptPassword("password")).thenReturn("encrypted");
        when(userPersistencePort.saveOwner(any(User.class))).thenReturn(expectedUser);

        // Act
        User result = userCase.saveOwner(adultUser);

        // Assert
        assertEquals(expectedUser, result);
        verify(passwordEncryptionPort).encryptPassword("password");
        verify(userPersistencePort).saveOwner(adultUser);
    }

    @Test
    public void SaveOwnerWithUnderageUserThrowsException() {
        UserCase userCase = new UserCase(userPersistencePort, passwordEncryptionPort);

        User underageUser = new User(1L, "John", "Doe", "123", "+1234567890",
                LocalDate.now().minusYears(16), "john@email.com", "password", new Role(2L, "OWNER", "description"));

        // Act & Assert
        InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class,
                () -> userCase.saveOwner(underageUser));

        assertEquals(String.format(INVALID_ARGUMENTS_MESSAGE, "date of birth"), exception.getMessage());
        verify(passwordEncryptionPort, never()).encryptPassword(any());
        verify(userPersistencePort, never()).saveOwner(any());
    }

    @Test
    public void TestReturnsTrueForValidOwner() {
        UserCase userCase = new UserCase(userPersistencePort, passwordEncryptionPort);
        String documentNumber = "12345";

        when(userPersistencePort.isOwner(documentNumber)).thenReturn(true);

        boolean result = userCase.isOwner(documentNumber);

        assertTrue(result);
        verify(userPersistencePort).isOwner(documentNumber);
    }

    @Test
    public void test_handles_empty_document_number() {
        UserCase userCase = new UserCase(userPersistencePort, passwordEncryptionPort);
        String emptyDocument = "";

        when(userPersistencePort.isOwner(emptyDocument)).thenReturn(false);

        boolean result = userCase.isOwner(emptyDocument);

        assertFalse(result);
        verify(userPersistencePort).isOwner(emptyDocument);
    }
}
