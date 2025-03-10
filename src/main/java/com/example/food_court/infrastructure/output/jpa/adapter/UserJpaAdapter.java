package com.example.food_court.infrastructure.output.jpa.adapter;

import com.example.food_court.domain.model.Role;
import com.example.food_court.domain.model.User;
import com.example.food_court.domain.spi.IUserPersistencePort;
import com.example.food_court.infrastructure.exception.ElementNotFoundException;
import com.example.food_court.infrastructure.exception.FieldAlreadyExistsException;
import com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages;
import com.example.food_court.infrastructure.output.jpa.entity.RoleEntity;
import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import com.example.food_court.infrastructure.output.jpa.mapper.IRoleEntityMapper;
import com.example.food_court.infrastructure.output.jpa.mapper.IUserEntityMapper;
import com.example.food_court.infrastructure.output.jpa.repository.IRoleRepository;
import com.example.food_court.infrastructure.output.jpa.repository.IUserRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class UserJpaAdapter implements IUserPersistencePort {

    private final IUserEntityMapper userEntityMapper;
    private final IUserRepository userRepository;
    private final IRoleRepository roleRepository;
    private final IRoleEntityMapper roleEntityMapper;

    @Override
    public User saveOwner(User user) {
        List<UserEntity> usersIdentification = userRepository.findAllByDocumentNumber(user.getDocumentNumber());
        if (!usersIdentification.isEmpty()) {
            throw new FieldAlreadyExistsException(String.format(ExceptionMessages.FIELD_ALREADY_EXISTS, "The identification document"));
        }
        Optional<UserEntity> usersEmail = userRepository.findByEmail(user.getEmail());
        if (usersEmail.isPresent()) {
            throw new FieldAlreadyExistsException(String.format(ExceptionMessages.FIELD_ALREADY_EXISTS, "The email"));
        }

        RoleEntity roleEntity = roleRepository.findById(2L)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format(ExceptionMessages.ELEMENT_NOT_FOUND, "role")
                ));

        Role role = roleEntityMapper.RoleEntitytoRole(roleEntity);
        user.setRole(role);

        UserEntity userEntity = userEntityMapper.UsertoUserEntity(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        return userEntityMapper.UserEntitytoUser(savedUserEntity);
    }

    @Override
    public boolean isOwner(String documentNumber) {
        return userRepository.existsByDocumentNumberAndRoleId(documentNumber, 2);
    }

    @Override
    public User saveEmployee(User user) {
        List<UserEntity> usersIdentification = userRepository.findAllByDocumentNumber(user.getDocumentNumber());
        if (!usersIdentification.isEmpty()) {
            throw new FieldAlreadyExistsException(String.format(ExceptionMessages.FIELD_ALREADY_EXISTS, "The identification document"));
        }
        Optional<UserEntity> usersEmail = userRepository.findByEmail(user.getEmail());
        if (usersEmail.isPresent()) {
            throw new FieldAlreadyExistsException(String.format(ExceptionMessages.FIELD_ALREADY_EXISTS, "The email"));
        }

        RoleEntity roleEntity = roleRepository.findById(3L)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format(ExceptionMessages.ELEMENT_NOT_FOUND, "role")
                ));

        Role role = roleEntityMapper.RoleEntitytoRole(roleEntity);
        user.setRole(role);

        UserEntity userEntity = userEntityMapper.UsertoUserEntity(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        return userEntityMapper.UserEntitytoUser(savedUserEntity);
    }

    @Override
    public void updateNit(String documentNumber, String nitRestaurant){
        UserEntity user = userRepository.findByDocumentNumber(documentNumber)
                .orElseThrow(() -> new NoSuchElementException("No se encontró un usuario con la cédula proporcionada."));

        user.setNit(nitRestaurant);
        userRepository.save(user);
    }

    @Override
    public User saveCustomer(User user) {
        List<UserEntity> usersIdentification = userRepository.findAllByDocumentNumber(user.getDocumentNumber());
        if (!usersIdentification.isEmpty()) {
            throw new FieldAlreadyExistsException(String.format(ExceptionMessages.FIELD_ALREADY_EXISTS, "The identification document"));
        }
        Optional<UserEntity> usersEmail = userRepository.findByEmail(user.getEmail());
        if (usersEmail.isPresent()) {
            throw new FieldAlreadyExistsException(String.format(ExceptionMessages.FIELD_ALREADY_EXISTS, "The email"));
        }

        RoleEntity roleEntity = roleRepository.findById(4L)
                .orElseThrow(() -> new ElementNotFoundException(
                        String.format(ExceptionMessages.ELEMENT_NOT_FOUND, "role")
                ));

        Role role = roleEntityMapper.RoleEntitytoRole(roleEntity);
        user.setRole(role);

        UserEntity userEntity = userEntityMapper.UsertoUserEntity(user);
        UserEntity savedUserEntity = userRepository.save(userEntity);

        return userEntityMapper.UserEntitytoUser(savedUserEntity);
    }

    @Override
    public Optional<User> getUserByDocument(String documentNumber) {
        return userRepository.findByDocumentNumber(documentNumber)
                .map(userEntityMapper::UserEntitytoUser);
    }
}
