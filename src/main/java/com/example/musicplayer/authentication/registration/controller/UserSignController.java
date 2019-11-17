package com.example.musicplayer.authentication.registration.controller;

import com.example.musicplayer.authentication.registration.model.UserRegistration;
import com.example.musicplayer.authentication.registration.service.UserRegistrationResult;
import com.example.musicplayer.authentication.registration.service.UserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
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
        return "/registrationAndLogin";
    }

    @PostMapping("/sign")
    public String register(UserRegistration userRegistration, Model model) {
        UserRegistrationResult userRegistrationResult = userRegistrationService.register(userRegistration);
        if (userRegistrationResult.hasErrors()) {
            model.addAttribute("userRegistration", userRegistration);
            model.addAttribute("userRegistrationResult", userRegistrationResult);
            return "/registrationAndLogin";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/login")
    public String getLoginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        model.addAttribute("userRegistration", new UserRegistration());
        if (error != null) {
            model.addAttribute("error", "Invalid username and/or password.");
        }
        return "registrationAndLogin.html";
    }

    @PostMapping("/login")
    public String login() {
        return "/";
    }
}