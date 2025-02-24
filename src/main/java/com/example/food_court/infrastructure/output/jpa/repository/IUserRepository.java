package com.example.food_court.infrastructure.output.jpa.repository;

import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUserRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findAllByDocumentNumber(String documentNumber);
    Optional<UserEntity> findByDocumentNumber(String documentNumber);
    Optional<UserEntity> findByEmail(String email);
    boolean existsByDocumentNumberAndRoleId(String documentNumber, int roleId);
}
