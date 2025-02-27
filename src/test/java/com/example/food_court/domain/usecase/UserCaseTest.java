package com.example.food_court.domain.usecase;

import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.exception.UserNotFoundException;
import com.example.food_court.domain.model.Role;
import com.example.food_court.domain.model.User;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.ISmallSquarePersistencePort;
import com.example.food_court.domain.spi.IUserPersistencePort;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages.INVALID_ARGUMENTS_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserCaseTest {

    @Mock
    private IUserPersistencePort userPersistencePort;

    @Mock
    private IPasswordEncryptionPort passwordEncryptionPort;

    @Mock
    private ISmallSquarePersistencePort smallSquarePersistencePort;

    @InjectMocks
    private UserCase userCase;

    @Test
    public void SaveOwnerWithValidAdultUserReturnsSavedUser() {
        User adultUser = new User(1L, "John", "Doe", "123", "+571234567890",
                LocalDate.now().minusYears(20), "john@email.com", "password", new Role(2L, "OWNER", "description"), "1112223334");
        User expectedUser = new User(1L, "John", "Doe", "123", "+571234567890",
                LocalDate.now().minusYears(20), "john@email.com", "encrypted", new Role(2L, "OWNER", "description"), "1112223334");

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
        User underageUser = new User(1L, "John", "Doe", "123", "+1234567890",
                LocalDate.now().minusYears(16), "john@email.com", "password", new Role(2L, "OWNER", "description"), "1112223334");

        // Act & Assert
        InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class,
                () -> userCase.saveOwner(underageUser));

        assertEquals(String.format(INVALID_ARGUMENTS_MESSAGE, "date of birth"), exception.getMessage());
        verify(passwordEncryptionPort, never()).encryptPassword(any());
        verify(userPersistencePort, never()).saveOwner(any());
    }

    @Test
    public void TestReturnsTrueForValidOwner() {
        String documentNumber = "12345";

        when(userPersistencePort.isOwner(documentNumber)).thenReturn(true);

        boolean result = userCase.isOwner(documentNumber);

        assertTrue(result);
        verify(userPersistencePort).isOwner(documentNumber);
    }

    @Test
    public void test_handles_empty_document_number() {
        String emptyDocument = "";

        when(userPersistencePort.isOwner(emptyDocument)).thenReturn(false);

        boolean result = userCase.isOwner(emptyDocument);

        assertFalse(result);
        verify(userPersistencePort).isOwner(emptyDocument);
    }

    @Test
    public void test_save_owner_with_invalid_phone_number() {
        LocalDate validBirthDate = LocalDate.now().minusYears(20);
        User user = new User(1L, "John", "Doe", "123", "invalidPhone", validBirthDate,
                "john@example.com", "password", new Role(2L, "OWNER", "description"), null);

        // Act & Assert
        assertThrows(InvalidArgumentsException.class, () -> {
            userCase.saveOwner(user);
        });
    }

    @Test
    public void test_save_employee_with_valid_adult_data() {
        User user = new User(1L, "John", "Doe", "123", "+571234567890",
                LocalDate.now().minusYears(20), "john@example.com", "password", new Role(3L, "Employee", "description"), "1112223334");

        String encryptedPassword = "encrypted_password";
        String nit = "123456";

        when(passwordEncryptionPort.encryptPassword("password")).thenReturn(encryptedPassword);
        when(smallSquarePersistencePort.validateNit()).thenReturn(nit);
        when(userPersistencePort.saveEmployee(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userCase.saveEmployee(user);

        // Assert
        assertNotNull(savedUser);
        verify(passwordEncryptionPort).encryptPassword("password");
        verify(smallSquarePersistencePort).validateNit();
        verify(userPersistencePort).saveEmployee(user);
    }

    @Test
    public void test_save_employee_with_null_birth_date_throws_exception() {
        User user = new User(1L, "John", "Doe", "123", "+1234567890",
                null, "john@example.com", "password", new Role(3L, "Employee", "description"), "1112223334");

        // Act & Assert
        InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class,
                () -> userCase.saveEmployee(user));

        assertEquals(String.format(INVALID_ARGUMENTS_MESSAGE, "date of birth"), exception.getMessage());
        verify(passwordEncryptionPort, never()).encryptPassword(any());
        verify(smallSquarePersistencePort, never()).validateNit();
        verify(userPersistencePort, never()).saveEmployee(any());
    }

    @Test
    public void test_save_employee_invalid_phone_number() {
        UserCase userCase = new UserCase(userPersistencePort, passwordEncryptionPort, smallSquarePersistencePort);

        User user = new User(1L, "John", "Doe", "123", "invalidPhone",
                LocalDate.now().minusYears(20), "john@example.com", "password", new Role(3L, "Employee", "description") , null);

        // Act & Assert
        assertThrows(InvalidArgumentsException.class, () -> {
            userCase.saveEmployee(user);
        });
    }

    @Test
    public void test_save_employee_with_null_phone_number_throws_exception() {
        UserCase userCase = new UserCase(userPersistencePort, passwordEncryptionPort, smallSquarePersistencePort);

        User user = new User(1L, "John", "Doe", "123", null,
                LocalDate.now().minusYears(20), "john@example.com", "password", new Role(3L, "Employee", "description"), null);

        // Act & Assert
        assertThrows(InvalidArgumentsException.class, () -> {
            userCase.saveEmployee(user);
        });
    }

    @Test
    public void test_update_nit_success() {
        String documentNumber = "123456789";
        String nitRestaurant = "987654321";

        userCase.updateNit(documentNumber, nitRestaurant);

        verify(userPersistencePort).updateNit(documentNumber, nitRestaurant);
    }

    @Test
    public void test_update_nit_null_document() {
        String documentNumber = null;
        String nitRestaurant = "987654321";

        userCase.updateNit(documentNumber, nitRestaurant);

        verify(userPersistencePort).updateNit(documentNumber, nitRestaurant);
    }

    @Test
    public void test_save_adult_customer_success() {
        LocalDate adultBirthDate = LocalDate.now().minusYears(20);
        User user = new User(1L, "John", "Doe", "123", "+571234567890",
                adultBirthDate, "john@example.com", "password", new Role(4L, "Customer", "description"), null);

        String encryptedPassword = "encrypted_password";
        when(passwordEncryptionPort.encryptPassword("password")).thenReturn(encryptedPassword);
        when(userPersistencePort.saveCustomer(any(User.class))).thenReturn(user);

        // Act
        User savedUser = userCase.saveCustomer(user);

        // Assert
        assertNotNull(savedUser);
        verify(passwordEncryptionPort).encryptPassword("password");
        verify(userPersistencePort).saveCustomer(user);
        assertEquals(encryptedPassword, savedUser.getPassword());
    }

    @Test
    public void test_save_underage_customer_throws_exception() {
        LocalDate underageBirthDate = LocalDate.now().minusYears(17);
        User user = new User(1L, "John", "Doe", "123", "+1234567890",
                underageBirthDate, "john@example.com", "password", new Role(4L, "Customer", "description"), null);

        // Act & Assert
        InvalidArgumentsException exception = assertThrows(InvalidArgumentsException.class,
                () -> userCase.saveCustomer(user));

        assertEquals(String.format(INVALID_ARGUMENTS_MESSAGE, "date of birth"), exception.getMessage());
        verify(passwordEncryptionPort, never()).encryptPassword(any());
        verify(userPersistencePort, never()).saveCustomer(any());
    }

    @Test
    public void test_save_customer_invalid_phone_number_throws_exception() {
        User invalidUser = new User(1L, "Jane", "Doe", "456", "123456",
                LocalDate.now().minusYears(25), "jane@example.com", "password", new Role(4L, "Customer", "description"), null);

        // Act & Assert
        assertThrows(InvalidArgumentsException.class, () -> {
            userCase.saveCustomer(invalidUser);
        });
    }

    @Test
    public void test_returns_phone_when_user_exists() {
        String documentNumber = "123456789";
        String expectedPhone = "3001234567";

        User mockUser = new User(1L, "John", "Doe", documentNumber, expectedPhone,
                LocalDate.now(), "john@email.com", "pass123", new Role(4L, "Customer", "description"), null);

        when(userPersistencePort.getUserByDocument(documentNumber))
                .thenReturn(Optional.of(mockUser));

        // Act
        String actualPhone = userCase.getPhoneByDocument(documentNumber);

        // Assert
        assertEquals(expectedPhone, actualPhone);
        verify(userPersistencePort).getUserByDocument(documentNumber);
    }

    @Test
    public void test_throws_exception_when_user_not_found() {
        String documentNumber = "nonexistent";

        when(userPersistencePort.getUserByDocument(documentNumber))
                .thenReturn(Optional.empty());


        // Act & Assert
        UserNotFoundException exception = assertThrows(UserNotFoundException.class,
                () -> userCase.getPhoneByDocument(documentNumber));

        assertEquals("User was not found.", exception.getMessage());
        verify(userPersistencePort).getUserByDocument(documentNumber);
    }
}
