package com.example.musicplayer.authentication.registration.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.registration.model.UserRegistration;
import com.example.musicplayer.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {
    private final UserRegistrationValidator userRegistrationValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserRegistrationService(UserRegistrationValidator userRegistrationValidator,
                                   UserService userService,
                                   PasswordEncoder passwordEncoder) {
        this.userRegistrationValidator = userRegistrationValidator;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public UserRegistrationResult register(UserRegistration userRegistration) {
        UserRegistrationValidationResult userRegistrationFormValidationResult = userRegistrationValidator.validate(userRegistration);
        if (!userRegistrationFormValidationResult.hasErrors()) {
            User user = userService.saveUser(registerUser(userRegistration));
            if (user != null) {
                return UserRegistrationResult.ok();
            } else {
                return UserRegistrationResult.fail(userRegistrationFormValidationResult.getErrors());
            }
        } else {
            return UserRegistrationResult.fail(userRegistrationFormValidationResult.getErrors());
        }
    }

    public User registerUser(UserRegistration userRegistration) {
        User user = new User();
        user.setUsername(userRegistration.getUsername());
        user.setName(userRegistration.getName());
        user.setSurname(userRegistration.getSurname());
        user.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
        user.setEmail(userRegistration.getEmail());
        return user;
    }
}