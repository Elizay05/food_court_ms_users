package com.example.food_court.application.mapper;

import com.example.food_court.application.dto.response.TokenResponse;
import com.example.food_court.domain.model.Token;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface ITokenResponseMapper {
    TokenResponse toTokenResponse(Token token);
}
