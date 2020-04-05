package com.example.musicplayer.registration.controller;

import com.example.musicplayer.registration.model.UserRegistration;
import com.example.musicplayer.registration.service.UserRegistrationResult;
import com.example.musicplayer.registration.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserSignController {
    private final UserRegistrationService userRegistrationService;

    @Autowired
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