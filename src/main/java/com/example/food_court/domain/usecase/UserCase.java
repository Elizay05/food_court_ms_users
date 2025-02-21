package com.example.food_court.domain.usecase;

import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.model.User;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.time.Period;

import static com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages.INVALID_ARGUMENTS_MESSAGE;

public class UserCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncryptionPort passwordEncryptionPort;

    public UserCase(IUserPersistencePort userPersistencePort, IPasswordEncryptionPort passwordEncryptionPort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncryptionPort = passwordEncryptionPort;
    }

    @Override
    public User saveOwner(User user) {
        if (!isAdult(user.getDateBirth())) {
            throw new InvalidArgumentsException(String.format(INVALID_ARGUMENTS_MESSAGE, "date of birth"));
        }
        String encryptedPassword = passwordEncryptionPort.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        return userPersistencePort.saveOwner(user);
    }

    public static boolean isAdult(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }
}
