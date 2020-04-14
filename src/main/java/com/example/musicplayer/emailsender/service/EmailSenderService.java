package com.example.musicplayer.emailsender.service;

import com.example.musicplayer.authentication.model.UserDto;
import com.example.musicplayer.authentication.service.UserService;
import com.example.musicplayer.emailsender.model.EmailLetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final PasswordRecover passwordRecover;
    private final WelcomeLetterService welcomeLetterService;
    private final Logger log = LoggerFactory.getLogger(EmailSenderService.class.getName());
    private final Set<String> allEmails;

    @Autowired
    public EmailSenderService(JavaMailSender javaMailSender,
                              PasswordRecover passwordRecover,
                              WelcomeLetterService welcomeLetterService,
                              UserService userService) {
        this.javaMailSender = javaMailSender;
        this.passwordRecover = passwordRecover;
        this.welcomeLetterService = welcomeLetterService;
        allEmails = userService.getAllEmails();
    }

    public boolean sendPasswordRecoverEmailMessage(String email) {
        boolean check = buildEmailMessage(passwordRecover.passwordRecoverEmailMessage(email, allEmails));
        if (check) {
            log.info("Email password recover was sent to: " + email);
        }
        return check;
    }

    public void sendWelcomeEmail(UserDto userDto) {
        buildEmailMessage(welcomeLetterService.welcomeEmailMessage(userDto));
    }

    private boolean buildEmailMessage(EmailLetter emailLetter) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(emailLetter.getEmail());
            mailMessage.setSubject(emailLetter.getSubject());
            mailMessage.setText(emailLetter.getText());
            javaMailSender.send(mailMessage);
            return true;
        } catch (Exception e) {
            log.error(e.toString());
            return false;
        }
    }
}