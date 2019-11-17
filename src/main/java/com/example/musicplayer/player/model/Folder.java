package com.example.musicplayer.player.model;

import org.springframework.stereotype.Component;

@Component
public class Folder {
    private int id;
    private int userId;
    private String path;

    public Folder(Integer id, String path) {
        this.id = id;
        this.path = path;
    }

    public Folder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}