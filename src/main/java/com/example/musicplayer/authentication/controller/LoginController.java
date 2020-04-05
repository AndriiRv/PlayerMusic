package com.example.musicplayer.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/login")
    public ResponseEntity<Object> getLoginPage(@RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            return new ResponseEntity<>("Invalid username and/or password.", HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<Object> performLogin(@RequestParam("username") String username,
                                               @RequestParam("password") String password,
                                               HttpServletRequest request) {
        try {
            request.login(username, password);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Invalid username \nand/or password.", HttpStatus.CONFLICT);
        }
    }
}
