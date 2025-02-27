package com.example.food_court.domain.usecase;

import com.example.food_court.domain.api.IUserServicePort;
import com.example.food_court.domain.exception.InvalidArgumentsException;
import com.example.food_court.domain.exception.UserNotFoundException;
import com.example.food_court.domain.model.User;
import com.example.food_court.domain.spi.IPasswordEncryptionPort;
import com.example.food_court.domain.spi.ISmallSquarePersistencePort;
import com.example.food_court.domain.spi.IUserPersistencePort;
import com.example.food_court.infrastructure.exceptionhandler.ExceptionMessages;

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
        if (!isValidPhoneNumber(user.getCellphoneNumber())) {
            throw new InvalidArgumentsException(String.format(INVALID_ARGUMENTS_MESSAGE, "phone number"));
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

        if (!isValidPhoneNumber(user.getCellphoneNumber())) {
            throw new InvalidArgumentsException(String.format(INVALID_ARGUMENTS_MESSAGE, "phone number"));
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

        if (!isValidPhoneNumber(user.getCellphoneNumber())) {
            throw new InvalidArgumentsException(String.format(INVALID_ARGUMENTS_MESSAGE, "phone number"));
        }

        String encryptedPassword = passwordEncryptionPort.encryptPassword(user.getPassword());
        user.setPassword(encryptedPassword);

        return userPersistencePort.saveCustomer(user);
    }

    @Override
    public String getPhoneByDocument(String documentNumber) {
        User user = userPersistencePort.getUserByDocument(documentNumber)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format(ExceptionMessages.ELEMENT_NOT_FOUND, "User")
                ));
        return user.getCellphoneNumber();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^\\+\\d{1,3}\\d{7,}$";
        return phoneNumber != null && phoneNumber.matches(regex);
    }
}
