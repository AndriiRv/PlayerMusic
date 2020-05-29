package com.example.musicplayer.sign.registration.controller;

import com.example.musicplayer.sign.registration.model.UserRegistration;
import com.example.musicplayer.sign.registration.service.UserRegistrationResult;
import com.example.musicplayer.sign.registration.service.UserRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserSignController {
    private final UserRegistrationService userRegistrationService;

    public UserSignController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/sign")
    public ResponseEntity<UserRegistrationResult> register(@ModelAttribute UserRegistration userRegistration) {
        UserRegistrationResult userRegistrationResult = userRegistrationService.register(userRegistration);
        if (userRegistrationResult.hasErrors()) {
            return new ResponseEntity<>(userRegistrationResult, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}