package com.example.food_court.application.mapper;

import com.example.food_court.application.dto.request.UserRequest;
import com.example.food_court.domain.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IUserRequestMapper {
    User UserRequestToUser(UserRequest userRequest);
}
