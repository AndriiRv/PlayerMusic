package com.example.musicplayer.authentication.registration.controller;

import com.example.musicplayer.authentication.registration.model.UserRegistration;
import com.example.musicplayer.authentication.registration.service.UserRegistrationResult;
import com.example.musicplayer.authentication.registration.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserRegistrationController {
    private final UserRegistrationService userRegistrationService;

    @Autowired
    public UserRegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userRegistration", new UserRegistration());
        return "/registration";
    }

    @PostMapping("/registration")
    public String register(UserRegistration userRegistration, Model model) {
        UserRegistrationResult userRegistrationResult = userRegistrationService.register(userRegistration);
        if (userRegistrationResult.hasErrors()) {
            model.addAttribute("userRegistration", userRegistration);
            model.addAttribute("userRegistrationResult", userRegistrationResult);
            return "/registration.html";
        } else {
            return "redirect:/login";
        }
    }
}