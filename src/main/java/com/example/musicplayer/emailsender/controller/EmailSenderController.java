package com.example.musicplayer.emailsender.controller;

import com.example.musicplayer.sign.user.model.UserDto;
import com.example.musicplayer.emailsender.service.EmailSenderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailSenderController {
    private final EmailSenderService emailSenderService;

    public EmailSenderController(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @PostMapping("/passwordRecover")
    public ResponseEntity<Object> sendPasswordRecoverEmail(@RequestParam String email) {
        boolean check = emailSenderService.sendPasswordRecoverEmailMessage(email);
        if (check) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Email not exist", HttpStatus.CONFLICT);
        }
    }

    @PostMapping("/welcomeLetter")
    @ResponseStatus(HttpStatus.OK)
    public void sendWelcomeLetter(@RequestBody UserDto userDto) {
        emailSenderService.sendWelcomeEmail(userDto);
    }
}