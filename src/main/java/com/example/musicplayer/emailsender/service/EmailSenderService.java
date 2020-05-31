package com.example.musicplayer.emailsender.service;

import com.example.musicplayer.emailsender.model.EmailLetter;
import com.example.musicplayer.sign.user.model.User;
import com.example.musicplayer.sign.user.model.UserDto;
import com.example.musicplayer.sign.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.musicplayer.config.ExceptionOutput.exceptionStacktraceToString;

@Component
public class EmailSenderService {
    private final JavaMailSender javaMailSender;
    private final PasswordRecover passwordRecover;
    private final WelcomeLetterService welcomeLetterService;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(EmailSenderService.class.getName());

    public EmailSenderService(JavaMailSender javaMailSender,
                              PasswordRecover passwordRecover,
                              WelcomeLetterService welcomeLetterService,
                              UserService userService) {
        this.javaMailSender = javaMailSender;
        this.passwordRecover = passwordRecover;
        this.welcomeLetterService = welcomeLetterService;
        this.userService = userService;
    }

    public boolean sendPasswordRecoverEmailMessage(String email) {
        Set<String> allEmails = userService.getAllUsers().stream().map(User::getEmail).collect(Collectors.toSet());
        boolean check = buildEmailMessage(passwordRecover.passwordRecoverEmailMessage(email, allEmails));
        if (check) {
            log.info("Email password recover was sent to: {}", email);
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
            log.error(exceptionStacktraceToString(e));
            return false;
        }
    }
}