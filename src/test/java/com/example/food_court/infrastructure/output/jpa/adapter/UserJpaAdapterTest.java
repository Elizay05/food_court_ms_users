package com.example.food_court.infrastructure.output.jpa.adapter;

import com.example.food_court.domain.model.Role;
import com.example.food_court.domain.model.User;
import com.example.food_court.infrastructure.exception.ElementNotFoundException;
import com.example.food_court.infrastructure.exception.FieldAlreadyExistsException;
import com.example.food_court.infrastructure.output.jpa.entity.RoleEntity;
import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import com.example.food_court.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.example.food_court.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.example.food_court.infrastructure.output.jpa.repository.IRoleRepository;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserJpaAdapterTest {

    @Test
    public void testSaveOwnerSuccess() {
        // Arrange
        IUserEntityMapper userEntityMapper = mock(IUserEntityMapper.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        IRoleRepository roleRepository = mock(IRoleRepository.class);
        IRoleEntityMapper roleEntityMapper = mock(IRoleEntityMapper.class);

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null);
        RoleEntity roleEntity = new RoleEntity(2L, "OWNER", "Restaurant Owner");
        Role role = new Role(2L, "OWNER", "Restaurant Owner");
        UserEntity userEntity = new UserEntity(1L, "John", "Doe", "123456", "1234567890", "2023-01-01", "john@email.com", "password", roleEntity);

        when(userRepository.findByDocumentNumber("123456")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(roleRepository.findById(2L)).thenReturn(Optional.of(roleEntity));
        when(roleEntityMapper.RoleEntitytoRole(roleEntity)).thenReturn(role);
        when(userEntityMapper.UsertoUserEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userEntityMapper.UserEntitytoUser(userEntity)).thenReturn(user);

        // Act
        User savedUser = userJpaAdapter.saveOwner(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        verify(userRepository).save(userEntity);
    }

    @Test
    public void testSaveOwnerExistingDocumentThrowsException() {
        // Arrange
        IUserEntityMapper userEntityMapper = mock(IUserEntityMapper.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        IRoleRepository roleRepository = mock(IRoleRepository.class);
        IRoleEntityMapper roleEntityMapper = mock(IRoleEntityMapper.class);

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null);
        UserEntity existingUser = new UserEntity(2L, "Jane", "Doe", "123456", "9876543210", "2023-01-01", "jane@email.com", "password", null);

        when(userRepository.findByDocumentNumber("123456")).thenReturn(Collections.singletonList(existingUser));

        // Act & Assert
        FieldAlreadyExistsException exception = assertThrows(FieldAlreadyExistsException.class, () -> {
            userJpaAdapter.saveOwner(user);
        });

        assertEquals("The identification document already exists.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testSaveOwnerExistingEmailThrowsException() {
        // Arrange
        IUserEntityMapper userEntityMapper = mock(IUserEntityMapper.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        IRoleRepository roleRepository = mock(IRoleRepository.class);
        IRoleEntityMapper roleEntityMapper = mock(IRoleEntityMapper.class);

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null);
        UserEntity existingUser = new UserEntity(2L, "Jane", "Doe", "654321", "9876543210", "2023-01-01", "john@email.com", "password", null);

        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        FieldAlreadyExistsException exception = assertThrows(FieldAlreadyExistsException.class, () -> {
            userJpaAdapter.saveOwner(user);
        });

        assertEquals("The email already exists.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testSaveOwnerRoleNotFoundThrowsException() {
        // Arrange
        IUserEntityMapper userEntityMapper = mock(IUserEntityMapper.class);
        IUserRepository userRepository = mock(IUserRepository.class);
        IRoleRepository roleRepository = mock(IRoleRepository.class);
        IRoleEntityMapper roleEntityMapper = mock(IRoleEntityMapper.class);

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", new Role(2L, "OWNER", "description"));

        when(userRepository.findByDocumentNumber(user.getDocumentNumber())).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        ElementNotFoundException exception = assertThrows(ElementNotFoundException.class, () -> {
            userJpaAdapter.saveOwner(user);
        });

        assertEquals("role was not found.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

}
