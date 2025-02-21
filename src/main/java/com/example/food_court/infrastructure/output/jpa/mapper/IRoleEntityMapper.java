package com.example.food_court.infrastructure.output.jpa.mapper;

import com.example.food_court.domain.model.Role;
import com.example.food_court.infrastructure.output.jpa.entity.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)

public interface IRoleEntityMapper {

    Role RoleEntitytoRole(RoleEntity roleEntity);

    RoleEntity RoletoRoleEntity (Role role);
}
