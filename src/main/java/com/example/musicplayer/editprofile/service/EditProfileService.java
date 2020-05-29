package com.example.musicplayer.editprofile.service;

import com.example.musicplayer.sign.user.model.User;
import com.example.musicplayer.sign.user.service.UserService;
import com.example.musicplayer.sign.registration.model.UserRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EditProfileService {
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(EditProfileService.class.getName());

    public EditProfileService(UserService userService) {
        this.userService = userService;
    }

    public boolean updateUser(User user, Integer userId, UserRegistration userRegistration) {
        if (!isAnyUserDataIsBlank(user, userRegistration)) {
            userService.updateUser(user, userId);
            log.info("'{}' - had updated his profile", user.getUsername());
            return true;
        }
        return false;
    }

    private boolean isAnyUserDataIsBlank(User user, UserRegistration userRegistration) {
        return user.getUsername().isBlank()
                || userRegistration.getPassword().isBlank()
                || user.getName().isBlank()
                || user.getEmail().isBlank();
    }
}