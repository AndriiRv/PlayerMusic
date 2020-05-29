package com.example.musicplayer.emailsender.service;

import com.example.musicplayer.sign.user.model.UserDto;
import com.example.musicplayer.emailsender.model.EmailLetter;
import org.springframework.stereotype.Service;

@Service
public class WelcomeLetterService {

    EmailLetter welcomeEmailMessage(UserDto userDto) {
        String subject = "Welcome!";
        String text = "Hello and welcome to Music Player! We hope you're will plenty satisfact your music needs and estimate how work our platform.\n"
                + "Your username: " + userDto.getUsername() + "\n"
                + "Enjoyable experience!\n\nPlayer Music (2020)";
        return new EmailLetter(userDto.getEmail(), subject, text);
    }
}