package com.example.musicplayer.player.model;

import org.springframework.stereotype.Component;

@Component
public class Playlist {
    private int id;
    private String title;

    public Playlist(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public Playlist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
