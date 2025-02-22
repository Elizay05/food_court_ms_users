package com.example.food_court.application.mapper;

import com.example.food_court.application.dto.request.AuthenticationRequest;
import com.example.food_court.domain.model.Authentication;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface IAuthenticationRequestMapper {
    Authentication ToAuthentication(AuthenticationRequest request);
}
