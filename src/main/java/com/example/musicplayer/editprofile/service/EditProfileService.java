package com.example.musicplayer.editprofile.service;

import com.example.musicplayer.sign.registration.model.UserRegistration;
import com.example.musicplayer.sign.user.model.User;
import com.example.musicplayer.sign.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public class EditProfileService {
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(EditProfileService.class.getName());

    public EditProfileService(UserService userService) {
        this.userService = userService;
    }

    public boolean updateUser(User user, Integer userId, UserRegistration userRegistration) {
        if (!isAnyUserDataIsBlank(user, userRegistration) && validateEmailOnCorrect(userRegistration)) {
            userService.updateUser(user, userId);
            log.info("'{}' - was updated his profile", user.getUsername());
            return true;
        }
        return false;
    }

    private boolean isAnyUserDataIsBlank(User user, UserRegistration userRegistration) {
        return user.getSurname().isBlank()
                || userRegistration.getPassword().isBlank()
                || user.getName().isBlank()
                || user.getEmail().isBlank();
    }

    private boolean validateEmailOnCorrect(UserRegistration userRegistration) {
        return !userRegistration.getEmail().isBlank() && Pattern.matches("^\\S+@\\S+$", userRegistration.getEmail());
    }
}