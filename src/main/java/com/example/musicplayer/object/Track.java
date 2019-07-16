package com.example.musicplayer.object;

import org.springframework.stereotype.Component;

@Component
public class Track {
    private Integer id;
    private String title;
    private Double size;
    private String length;
    private String pathToFolder;
    private String date;

    public Track(Integer id, String title, Double size, String length, String pathToFolder, String date) {
        this.id = id;
        this.title = title;
        this.size = size;
        this.length = length;
        this.pathToFolder = pathToFolder;
        this.date = date;
    }

    public Track() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getSize() {
        return size;
    }

    public void setSize(Double size) {
        this.size = size;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getPathToFolder() {
        return pathToFolder;
    }

    public void setPathToFolder(String pathToFolder) {
        this.pathToFolder = pathToFolder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}