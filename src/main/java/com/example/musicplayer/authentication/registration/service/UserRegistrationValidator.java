package com.example.musicplayer.authentication.registration.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.registration.model.UserRegistration;
import com.example.musicplayer.authentication.service.UserService;
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

    private void validatePassword(UserRegistration userRegistration, UserRegistrationValidationResult userRegistrationValidationResult) {
        String password = userRegistration.getPassword();
        assert password != null;
        if (!password.equals(userRegistration.getPasswordConfirmation())) {
            userRegistrationValidationResult.addError("Password must match password confirmation");
        }
    }

    private void validateUsername(UserRegistration userRegistration, UserRegistrationValidationResult userRegistrationValidationResult) {
        String username = userRegistration.getUsername();
        User user = userService.getUserByUsername(username);
        if (user != null) {
            userRegistrationValidationResult.addError("Username is already in use");
        }
    }

    private void validateEmail(UserRegistration userRegistration, UserRegistrationValidationResult userRegistrationValidationResult) {
        List<User> allUsers = userService.getAllUsers();
        for (User user : allUsers) {
            if (user.getEmail().equals(userRegistration.getEmail())) {
                userRegistrationValidationResult.addError("Email is already in use");
            }
        }
    }
}