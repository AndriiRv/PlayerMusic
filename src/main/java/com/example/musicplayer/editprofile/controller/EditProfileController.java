package com.example.musicplayer.editprofile.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.editprofile.service.EditProfileService;
import com.example.musicplayer.registration.model.UserRegistration;
import com.example.musicplayer.registration.service.UserRegistrationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EditProfileController {
    private final EditProfileService editProfileService;
    private final UserRegistrationService userRegistrationService;

    public EditProfileController(EditProfileService editProfileService, UserRegistrationService userRegistrationService) {
        this.editProfileService = editProfileService;
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping("/edit")
    public ResponseEntity<Object> editProfileByUser(@AuthenticationPrincipal User user, UserRegistration userRegistration) {
        boolean isUserUpdated = editProfileService.updateUser(userRegistrationService.registerUser(userRegistration), user.getId(), userRegistration);
        if (isUserUpdated) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}