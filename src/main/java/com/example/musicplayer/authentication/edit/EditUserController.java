package com.example.musicplayer.authentication.edit;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.registration.model.UserRegistration;
import com.example.musicplayer.authentication.registration.service.UserRegistrationService;
import com.example.musicplayer.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/edit")
public class EditUserController {
    private final UserService userService;
    private final UserRegistrationService userRegistrationService;

    @Autowired
    public EditUserController(UserService userService, UserRegistrationService userRegistrationService) {
        this.userService = userService;
        this.userRegistrationService = userRegistrationService;
    }

    @GetMapping
    public String getEditForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("name", user.getName());
        model.addAttribute("surname", user.getSurname());
        model.addAttribute("email", user.getEmail());

        model.addAttribute("user", new UserRegistration());
        return "/edit";
    }

    @PostMapping
    public String changeDataToUser(@AuthenticationPrincipal User user, UserRegistration userRegistration) {
        userService.updateUser(userRegistrationService.registerUser(userRegistration), user.getId());
        return "redirect:/edit";
    }
}