package com.example.musicplayer.sign.authentication.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {
    private final Logger log = LoggerFactory.getLogger(LoginController.class.getName());

    @GetMapping
    public ResponseEntity<Object> getLoginPage(@RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            return new ResponseEntity<>("Invalid username and/or password.", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> performLogin(@RequestParam("username") String username,
                                               @RequestParam("password") String password,
                                               HttpServletRequest request) {
        try {
            request.login(username, password);
            log.info("'{}' was sign in successful", username);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username \nand/or password.", HttpStatus.CONFLICT);
        }
    }
}