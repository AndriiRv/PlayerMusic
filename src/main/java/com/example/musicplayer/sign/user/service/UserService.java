package com.example.musicplayer.sign.user.service;

import com.example.musicplayer.sign.user.model.User;
import com.example.musicplayer.sign.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    public User saveUser(User user) {
        return userRepository.saveUser(user);
    }

    public void updateUser(User user, int userId) {
        userRepository.updateUser(user, userId);
    }

    public void updatePasswordByEmailRecover(String email, String password) {
        userRepository.updatePasswordByEmailRecover(email, password);
    }

    public User getUserByUsername(String username) {
        return userRepository.getUserByUsername(username);
    }

    public User getUserByUserId(int userId) {
        return userRepository.getUserByUserId(userId);
    }
}