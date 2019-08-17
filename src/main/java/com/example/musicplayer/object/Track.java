package com.example.musicplayer.object;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
public class Track {
    private Integer id;
    private String fullTitle;
    private String singer;
    private String title;
    private Double size;
    private String length;
    private String pathToFolder;
    private LocalDateTime dateTime;
    private LocalDate date;
    private LocalTime time;

    public Track(Integer id, String fullTitle, String singer, String title, Double size, String length,
                 String pathToFolder, LocalDateTime dateTime, LocalDate date, LocalTime time) {
        this.id = id;
        this.fullTitle = fullTitle;
        this.singer = singer;
        this.title = title;
        this.size = size;
        this.length = length;
        this.pathToFolder = pathToFolder;
        this.dateTime = dateTime;
        this.date = date;
        this.time = time;
    }

    public Track() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public void setFullTitle(String fullTitle) {
        this.fullTitle = fullTitle;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
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

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}