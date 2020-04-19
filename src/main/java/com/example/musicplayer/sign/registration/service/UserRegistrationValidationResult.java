package com.example.musicplayer.sign.registration.service;

import java.util.ArrayList;
import java.util.List;

class UserRegistrationValidationResult {
    private final List<String> errors = new ArrayList<>();

    void addError(String error) {
        errors.add(error);
    }

    List<String> getErrors() {
        return errors;
    }

    boolean hasErrors() {
        return !errors.isEmpty();
    }
}
