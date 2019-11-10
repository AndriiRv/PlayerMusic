package com.example.musicplayer.model;

import org.springframework.stereotype.Component;

@Component
public class Folder {
    private Integer id;
    private String path;

    public Folder(Integer id, String path) {
        this.id = id;
        this.path = path;
    }

    public Folder() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}