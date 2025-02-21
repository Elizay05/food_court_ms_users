package com.example.food_court.infrastructure.output.jpa.mapper;

import com.example.food_court.domain.model.User;
import com.example.food_court.infrastructure.output.jpa.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserEntityMapper {

    User UserEntitytoUser(UserEntity userEntity);

    UserEntity UsertoUserEntity(User user);
}