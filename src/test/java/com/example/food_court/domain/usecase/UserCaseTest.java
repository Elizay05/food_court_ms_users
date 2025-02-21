package com.example.food_court.domain.usecase;

import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.model.Role;
import com.example.food_court.domain.model.User;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.IUserPersistencePort;
import com.example.food_court.infrastructure.exception.ElementNotFoundException;
import com.example.food_court.infrastructure.exception.FieldAlreadyExistsException;
import com.example.food_court.infrastructure.output.jpa.adapter.UserJpaAdapter;
import com.example.food_court.infrastructure.output.jpa.entity.RoleEntity;
import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import com.example.food_court.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.example.food_court.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.example.food_court.infrastructure.output.jpa.repository.IRoleRepository;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.Optional;

import static com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages.INVALID_ARGUMENTS_MESSAGE;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserCaseTest {

    @Test
    public void save_owner_with_valid_adult_user_returns_saved_user() {
        // Arrange
        IUserPersistencePort userPersistencePort = mock(IUserPersistencePort.class);
        IPasswordEncryptionPort passwordEncryptionPort = mock(IPasswordEncryptionPort.class);
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
    public void save_owner_with_underage_user_throws_exception() {
        // Arrange
        IUserPersistencePort userPersistencePort = mock(IUserPersistencePort.class);
        IPasswordEncryptionPort passwordEncryptionPort = mock(IPasswordEncryptionPort.class);
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
}
