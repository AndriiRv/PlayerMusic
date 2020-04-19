package com.example.musicplayer.editprofile.service;

import com.example.musicplayer.sign.authentication.model.User;
import com.example.musicplayer.sign.authentication.service.UserService;
import com.example.musicplayer.sign.registration.model.UserRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditProfileService {
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(EditProfileService.class.getName());

    @Autowired
    public EditProfileService(UserService userService) {
        this.userService = userService;
    }

    public boolean updateUser(User user, Integer userId, UserRegistration userRegistration) {
        if (!isAnyUserDataIsBlank(user, userRegistration)) {
            userService.updateUser(user, userId);
            log.info("'" + user.getUsername() + "' - was update his profile");
            return true;
        }
        return false;
    }

    private boolean isAnyUserDataIsBlank(User user, UserRegistration userRegistration) {
        if (user.getUsername().isBlank()) {
            return true;
        } else if (userRegistration.getPassword().isBlank()) {
            return true;
        } else if (user.getName().isBlank()) {
            return true;
        } else if (user.getSurname().isBlank()) {
            return true;
        } else {
            return user.getEmail().isBlank();
        }
    }
}