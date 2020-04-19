package com.example.musicplayer.emailsender.service;

import com.example.musicplayer.emailsender.model.EmailLetter;
import com.example.musicplayer.sign.authentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class PasswordRecover {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private static final int SIZE_OF_PASSWORD = 12;

    @Autowired
    public PasswordRecover(UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    EmailLetter passwordRecoverEmailMessage(String email, Set<String> allEmails) {
        if (allEmails.stream().anyMatch((e -> e.equals(email)))) {
            String newPassword = getPassword();
            userService.updatePasswordByEmail(email, passwordEncoder.encode(newPassword));
            String subject = "Password Recover";
            String text = "Your new password: " + newPassword + " \nYou can change it, if you go ahead to edit your account\nPlayer Music (2020)";
            return new EmailLetter(email, subject, text);
        }
        return null;
    }

    private String getPassword() {
        char[] alphabet = getAlphabet();
        int[] numbers = getNumbers();

        Random random = new Random();

        List<String> list = new ArrayList<>();
        for (int i = 0; i < SIZE_OF_PASSWORD / 2; i++) {
            alphabet[i] = (char) (97 + random.nextInt(alphabet.length));
            list.add(String.valueOf(alphabet[i]));
            if (i < numbers.length) {
                numbers[i] = random.nextInt(numbers.length);
                list.add(String.valueOf(numbers[i]));
            }
        }

        Collections.shuffle(list);

        StringBuilder stringBuffer = new StringBuilder();
        for (String symbol : list) {
            stringBuffer.append(symbol);
        }
        return stringBuffer.toString();
    }

    private char[] getAlphabet() {
        char[] alpha = new char[26];
        for (int i = 0; i < 26; i++) {
            alpha[i] = (char) (97 + i);
        }
        return alpha;
    }

    private int[] getNumbers() {
        int[] numbers = new int[10];
        for (int i = 0; i < numbers.length; i++) {
            numbers[i] = i;
        }
        return numbers;
    }
}