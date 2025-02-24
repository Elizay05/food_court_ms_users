package com.example.food_court.domain.usecase;

import com.example.food_court.domain.api.IAuthenticationServicePort;
import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.model.Authentication;
import com.example.food_court.domain.model.Token;
import com.example.food_court.domain.spi.IAuthenticationPersistencePort;

import static com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages.INVALID_ARGUMENTS_MESSAGE;

public class AuthenticationUseCase implements IAuthenticationServicePort {

    private final IAuthenticationPersistencePort authenticationPersistencePort;

    public AuthenticationUseCase(IAuthenticationPersistencePort authenticationPersistencePort) {
        this.authenticationPersistencePort = authenticationPersistencePort;
    }

    @Override
    public Token authenticate(Authentication authentication) {
        if (!isValidEmail(authentication.getEmail())) {
            throw new InvalidArgumentsException(String.format(INVALID_ARGUMENTS_MESSAGE, "email"));
        }
        return authenticationPersistencePort.authenticate(authentication);
    }

    public static boolean isValidEmail(String email) {
        String regex = "^[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?$";
        return email.matches(regex);
    }
}
