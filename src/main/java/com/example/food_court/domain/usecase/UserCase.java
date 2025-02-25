package com.example.food_court.domain.usecase;

import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.model.User;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.ISmallSquarePersistencePort;
import com.example.food_court.domain.spi.IUserPersistencePort;

import java.time.LocalDate;
import java.time.Period;

import static com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages.INVALID_ARGUMENTS_MESSAGE;

public class UserCase implements IUserServicePort {

    private final IUserPersistencePort userPersistencePort;
    private final IPasswordEncryptionPort passwordEncryptionPort;
    private final ISmallSquarePersistencePort smallSquarePersistencePort;

    public UserCase(IUserPersistencePort userPersistencePort, IPasswordEncryptionPort passwordEncryptionPort, ISmallSquarePersistencePort smallSquarePersistencePort) {
        this.userPersistencePort = userPersistencePort;
        this.passwordEncryptionPort = passwordEncryptionPort;
        this.smallSquarePersistencePort = smallSquarePersistencePort;
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

    @Override
    public boolean isOwner(String documentNumber) {
        return userPersistencePort.isOwner(documentNumber);
    }

    public static boolean isAdult(LocalDate birthDate) {
        if (birthDate == null) {
            return false;
        }
        return Period.between(birthDate, LocalDate.now()).getYears() >= 18;
    }

    @Override
    public User saveEmployee(User user) {
        if (!isAdult(user.getDateBirth())) {
            throw new InvalidArgumentsException(String.format(INVALID_ARGUMENTS_MESSAGE, "date of birth"));
        }

        String encryptedPassword = passwordEncryptionPort.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        String nit = smallSquarePersistencePort.validateNit();
        user.setNit(nit);

        return userPersistencePort.saveEmployee(user);
    }

    @Override
    public void updateNit(String documentNumber, String nitRestaurant){
        userPersistencePort.updateNit(documentNumber, nitRestaurant);
    }

    @Override
    public User saveCustomer(User user) {
        if (!isAdult(user.getDateBirth())) {
            throw new InvalidArgumentsException(String.format(INVALID_ARGUMENTS_MESSAGE, "date of birth"));
        }

        String encryptedPassword = passwordEncryptionPort.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        return userPersistencePort.saveCustomer(user);
    }
}
