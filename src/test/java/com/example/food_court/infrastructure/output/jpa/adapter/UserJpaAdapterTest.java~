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
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserJpaAdapterTest {

    @Mock
    private IUserEntityMapper userEntityMapper;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private IRoleEntityMapper roleEntityMapper;

    @InjectMocks
    private UserJpaAdapter userJpaAdapter;

    @Test
    public void testSaveOwnerSuccess() {

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null, "1112223334");
        RoleEntity roleEntity = new RoleEntity(2L, "OWNER", "Restaurant Owner");
        Role role = new Role(2L, "OWNER", "Restaurant Owner");
        UserEntity userEntity = new UserEntity(1L, "John", "Doe", "123456", "1234567890", "2023-01-01", "john@email.com", "password", roleEntity, "1127563333");

        when(userRepository.findAllByDocumentNumber("123456")).thenReturn(Collections.emptyList());
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

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null, "1112223334");
        UserEntity existingUser = new UserEntity(2L, "Jane", "Doe", "123456", "9876543210", "2023-01-01", "jane@email.com", "password", null, "1127563333");

        when(userRepository.findAllByDocumentNumber("123456")).thenReturn(Collections.singletonList(existingUser));

        // Act & Assert
        FieldAlreadyExistsException exception = assertThrows(FieldAlreadyExistsException.class, () -> {
            userJpaAdapter.saveOwner(user);
        });

        assertEquals("The identification document already exists.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testSaveOwnerExistingEmailThrowsException() {

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null, "1112223334");
        UserEntity existingUser = new UserEntity(2L, "Jane", "Doe", "654321", "9876543210", "2023-01-01", "john@email.com", "password", null, "1127563333");

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

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", new Role(2L, "OWNER", "description"), "1112223334");

        when(userRepository.findAllByDocumentNumber(user.getDocumentNumber())).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        ElementNotFoundException exception = assertThrows(ElementNotFoundException.class, () -> {
            userJpaAdapter.saveOwner(user);
        });

        assertEquals("role was not found.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testReturnsTrueWhenDocumentExistsWithRoleId2() {
        // Arrange
        String documentNumber = "12345";
        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);
        when(userRepository.existsByDocumentNumberAndRoleId(documentNumber, 2)).thenReturn(true);

        // Act
        boolean result = userJpaAdapter.isOwner(documentNumber);

        // Assert
        assertTrue(result);
        verify(userRepository).existsByDocumentNumberAndRoleId(documentNumber, 2);
    }

    @Test
    public void testReturnsFalseForEmptyDocumentNumber() {
        // Arrange
        String documentNumber = "";
        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);
        when(userRepository.existsByDocumentNumberAndRoleId(documentNumber, 2)).thenReturn(false);

        // Act
        boolean result = userJpaAdapter.isOwner(documentNumber);

        // Assert
        assertFalse(result);
        verify(userRepository).existsByDocumentNumberAndRoleId(documentNumber, 2);
    }

    @Test
    public void test_save_employee_success() {
        // Arrange
        User user = new User(1L, "John", "Doe", "123", "1234567890",
                LocalDate.now(), "john@email.com", "password", null, null);

        RoleEntity roleEntity = RoleEntity.builder()
                .id(3L)
                .name("Employee")
                .description("Employee Role")
                .build();

        Role role = new Role(3L, "Employee", "Employee Role");

        UserEntity savedUserEntity = UserEntity.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .documentNumber("123")
                .email("john@email.com")
                .role(roleEntity)
                .build();

        when(userRepository.findAllByDocumentNumber("123")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(roleRepository.findById(3L)).thenReturn(Optional.of(roleEntity));
        when(roleEntityMapper.RoleEntitytoRole(roleEntity)).thenReturn(role);
        when(userEntityMapper.UsertoUserEntity(user)).thenReturn(savedUserEntity);
        when(userRepository.save(savedUserEntity)).thenReturn(savedUserEntity);
        when(userEntityMapper.UserEntitytoUser(savedUserEntity)).thenReturn(user);

        // Act
        User result = userJpaAdapter.saveEmployee(user);

        // Assert
        assertNotNull(result);
        assertEquals("John", result.getName());
        assertEquals("123", result.getDocumentNumber());
        assertEquals("john@email.com", result.getEmail());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void test_save_employee_existing_document() {
        // Arrange
        User user = new User(1L, "John", "Doe", "123", "1234567890",
                LocalDate.now(), "john@email.com", "password", null, null);

        UserEntity existingUser = UserEntity.builder()
                .id(2L)
                .documentNumber("123")
                .build();

        when(userRepository.findAllByDocumentNumber("123"))
                .thenReturn(Arrays.asList(existingUser));

        // Act & Assert
        assertThrows(FieldAlreadyExistsException.class, () -> {
            userJpaAdapter.saveEmployee(user);
        });

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void test_save_employee_role_not_found() {
        // Arrange
        User user = new User(1L, "Jane", "Doe", "456", "0987654321",
                LocalDate.now(), "jane@email.com", "password", null, null);

        when(userRepository.findAllByDocumentNumber("456")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("jane@email.com")).thenReturn(Optional.empty());
        when(roleRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ElementNotFoundException.class, () -> {
            userJpaAdapter.saveEmployee(user);
        });

        verify(roleRepository).findById(3L);
    }

    @Test
    public void test_save_employee_with_existing_email() {
        // Arrange
        User user = new User(1L, "Jane", "Doe", "456", "0987654321",
                LocalDate.now(), "jane@email.com", "password", null, null);

        UserEntity existingUserEntity = UserEntity.builder()
                .id(2L)
                .name("Existing")
                .lastName("User")
                .documentNumber("789")
                .email("jane@email.com")
                .build();

        when(userRepository.findAllByDocumentNumber("456")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("jane@email.com")).thenReturn(Optional.of(existingUserEntity));

        // Act & Assert
        assertThrows(FieldAlreadyExistsException.class, () -> {
            userJpaAdapter.saveEmployee(user);
        });

        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void test_update_nit_success() {
        // Arrange
        String documentNumber = "123456789";
        String newNit = "987654321";

        UserEntity mockUser = new UserEntity();
        mockUser.setDocumentNumber(documentNumber);
        mockUser.setNit("oldNit");

        when(userRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(UserEntity.class)))
                .thenReturn(mockUser);

        // Act
        userJpaAdapter.updateNit(documentNumber, newNit);

        // Assert
        verify(userRepository).findByDocumentNumber(documentNumber);
        verify(userRepository).save(mockUser);
        assertEquals(newNit, mockUser.getNit());
    }

    @Test
    public void test_update_nit_user_not_found() {
        // Arrange
        String documentNumber = "nonexistent";
        String newNit = "987654321";

        when(userRepository.findByDocumentNumber(documentNumber))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> {
            userJpaAdapter.updateNit(documentNumber, newNit);
        });

        verify(userRepository).findByDocumentNumber(documentNumber);
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    public void testSaveCustomerSuccess() {

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        Role role = new Role(4L, "CUSTOMER", "Description for customer");
        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", role, "1112223334");
        RoleEntity roleEntity = new RoleEntity(4L, "CUSTOMER", "Description for customer");
        UserEntity userEntity = new UserEntity(1L, "John", "Doe", "123456", "1234567890", "2023-01-01", "john@email.com", "password", roleEntity, "1127563333");

        when(userRepository.findAllByDocumentNumber("123456")).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.empty());
        when(roleRepository.findById(4L)).thenReturn(Optional.of(roleEntity));
        when(roleEntityMapper.RoleEntitytoRole(roleEntity)).thenReturn(role);
        when(userEntityMapper.UsertoUserEntity(user)).thenReturn(userEntity);
        when(userRepository.save(userEntity)).thenReturn(userEntity);
        when(userEntityMapper.UserEntitytoUser(userEntity)).thenReturn(user);

        // Act
        User savedUser = userJpaAdapter.saveCustomer(user);

        // Assert
        assertNotNull(savedUser);
        assertEquals(user.getId(), savedUser.getId());
        assertEquals(user.getEmail(), savedUser.getEmail());
        verify(userRepository).save(userEntity);
    }

    @Test
    public void testSaveCustomerExistingDocumentThrowsException() {

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null, "1112223334");
        UserEntity existingUser = new UserEntity(2L, "Jane", "Doe", "123456", "9876543210", "2023-01-01", "jane@email.com", "password", null, "1127563333");

        when(userRepository.findAllByDocumentNumber("123456")).thenReturn(Collections.singletonList(existingUser));

        // Act & Assert
        FieldAlreadyExistsException exception = assertThrows(FieldAlreadyExistsException.class, () -> {
            userJpaAdapter.saveCustomer(user);
        });

        assertEquals("The identification document already exists.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testSaveCustomerExistingEmailThrowsException() {

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", null, "1112223334");
        UserEntity existingUser = new UserEntity(2L, "Jane", "Doe", "654321", "9876543210", "2023-01-01", "john@email.com", "password", null, "1127563333");

        when(userRepository.findByEmail("john@email.com")).thenReturn(Optional.of(existingUser));

        // Act & Assert
        FieldAlreadyExistsException exception = assertThrows(FieldAlreadyExistsException.class, () -> {
            userJpaAdapter.saveCustomer(user);
        });

        assertEquals("The email already exists.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void testSaveCustomerRoleNotFoundThrowsException() {

        UserJpaAdapter userJpaAdapter = new UserJpaAdapter(userEntityMapper, userRepository, roleRepository, roleEntityMapper);

        User user = new User(1L, "John", "Doe", "123456", "1234567890", LocalDate.now(), "john@email.com", "password", new Role(4L, "CUSTOMER", "Description for customer"), "1112223334");

        when(userRepository.findAllByDocumentNumber(user.getDocumentNumber())).thenReturn(Collections.emptyList());
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(roleRepository.findById(4L)).thenReturn(Optional.empty());

        // Act & Assert
        ElementNotFoundException exception = assertThrows(ElementNotFoundException.class, () -> {
            userJpaAdapter.saveCustomer(user);
        });

        assertEquals("role was not found.", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
