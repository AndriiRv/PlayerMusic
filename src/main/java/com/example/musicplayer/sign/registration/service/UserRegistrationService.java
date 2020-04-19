package com.example.musicplayer.sign.registration.service;

import com.example.musicplayer.sign.authentication.model.User;
import com.example.musicplayer.sign.authentication.service.UserService;
import com.example.musicplayer.sign.registration.model.UserRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.musicplayer.sign.registration.service.UserRegistrationValidator.ifAnyHasUserDataIsBlank;

@Service
public class UserRegistrationService {
    private final UserRegistrationValidator userRegistrationValidator;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final Logger logger = LoggerFactory.getLogger(UserRegistrationService.class);

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

        if (ifAnyHasUserDataIsBlank(userRegistration)) {
            userRegistrationFormValidationResult.addError("*Please fill all of your data");
        }

        if (!userRegistrationFormValidationResult.hasErrors()) {
            User user = userService.saveUser(registerUser(userRegistration));
            if (user != null) {
                logRegisterUser(userRegistration, " - registered successfully");
                return UserRegistrationResult.ok();
            } else {
                logRegisterUser(userRegistration, " - got a mistake during registered");
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

    private void logRegisterUser(UserRegistration userRegistration, String result) {
        logger.info(userRegistration.getName() + " " + userRegistration.getSurname() + " with username: '"
                + userRegistration.getUsername() + "'" + result);
    }
}