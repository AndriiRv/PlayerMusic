package com.example.musicplayer.registration.controller;

import com.example.musicplayer.registration.model.UserRegistration;
import com.example.musicplayer.registration.service.UserRegistrationResult;
import com.example.musicplayer.registration.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserSignController {
    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserSignController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/sign")
    public String registration(Model model) {
        model.addAttribute("userRegistration", new UserRegistration());
        return "/index";
    }

    @PostMapping("/sign")
    public ResponseEntity<UserRegistrationResult> register(UserRegistration userRegistration, Model model) {
        UserRegistrationResult userRegistrationResult = userRegistrationService.register(userRegistration);
        if (userRegistrationResult.hasErrors()) {
            return new ResponseEntity<>(userRegistrationResult, HttpStatus.CONFLICT);
        } else {
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }
}
