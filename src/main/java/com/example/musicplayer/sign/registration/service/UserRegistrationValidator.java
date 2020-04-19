package com.example.musicplayer.sign.registration.service;

import com.example.musicplayer.sign.authentication.model.User;
import com.example.musicplayer.sign.authentication.service.UserService;
import com.example.musicplayer.sign.registration.model.UserRegistration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserRegistrationValidator {
    private final UserService userService;

    @Autowired
    public UserRegistrationValidator(UserService userService) {
        this.userService = userService;
    }

    UserRegistrationValidationResult validate(UserRegistration userRegistration) {
        UserRegistrationValidationResult userRegistrationFormValidationResult = new UserRegistrationValidationResult();
        validatePassword(userRegistration, userRegistrationFormValidationResult);
        validateUsername(userRegistration, userRegistrationFormValidationResult);
        validateEmail(userRegistration, userRegistrationFormValidationResult);
        return userRegistrationFormValidationResult;
    }

    public static boolean ifAnyHasUserDataIsBlank(UserRegistration userRegistration) {
        return isRegistrationDataIsBlank(userRegistration.getName()) ||
                isRegistrationDataIsBlank(userRegistration.getSurname()) ||
                isRegistrationDataIsBlank(userRegistration.getEmail()) ||
                isRegistrationDataIsBlank(userRegistration.getUsername()) ||
                isRegistrationDataIsBlank(userRegistration.getPassword()) ||
                isRegistrationDataIsBlank(userRegistration.getPasswordConfirmation());
    }

    private static boolean isRegistrationDataIsBlank(String registrationData) {
        return registrationData == null || registrationData.isBlank();
    }

    private void validatePassword(UserRegistration userRegistration, UserRegistrationValidationResult userRegistrationValidationResult) {
        String password = userRegistration.getPassword();
        assert password != null;
        if (!password.equals(userRegistration.getPasswordConfirmation())) {
            userRegistrationValidationResult.addError("*Password must match\n password confirmation");
        }
    }

    private void validateUsername(UserRegistration userRegistration, UserRegistrationValidationResult userRegistrationValidationResult) {
        String username = userRegistration.getUsername();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            userRegistrationValidationResult.addError("*Username is already in use");
        }
    }

    private void validateEmail(UserRegistration userRegistration, UserRegistrationValidationResult userRegistrationValidationResult) {
        List<User> allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            if (user.getEmail().equals(userRegistration.getEmail())) {
                userRegistrationValidationResult.addError("*Email is already in use");
            }
        }
    }
}