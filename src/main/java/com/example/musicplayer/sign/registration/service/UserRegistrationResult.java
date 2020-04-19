package com.example.musicplayer.sign.registration.service;

import java.util.Collections;
import java.util.List;

public class UserRegistrationResult {
    private final List<String> errors;

    static UserRegistrationResult ok() {
        return new UserRegistrationResult(Collections.emptyList());
    }

    static UserRegistrationResult fail(List<String> errors) {
        return new UserRegistrationResult(errors);
    }

    private UserRegistrationResult(List<String> errors) {
        this.errors = errors;
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public List<String> getErrors() {
        return errors;
    }
}
