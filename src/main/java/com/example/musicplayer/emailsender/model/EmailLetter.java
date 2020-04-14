package com.example.musicplayer.emailsender.model;

public class EmailLetter {
    private int id;
    private String email;
    private String subject;
    private String text;

    public EmailLetter(String email, String subject, String text) {
        this.email = email;
        this.subject = subject;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}