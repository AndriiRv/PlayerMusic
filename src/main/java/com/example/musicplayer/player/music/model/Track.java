package com.example.musicplayer.player.music.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;

public class Track {
    private Integer id;
    private String fullTitle;
    private String singer;
    private String title;
    private Double size;
    private String length;
    private LocalDateTime dateTime;
    private LocalDate date;
    private LocalTime time;
    private byte[] byteOfPicture;
    private String albumTitle;
    private String year;
    private String genre;

    public Track(Integer id, String fullTitle, String title, String singer, Double size,
                 String length, LocalDateTime dateTime, LocalDate date, LocalTime time,
                 byte[] byteOfPicture, String albumTitle, String year, String genre) {
        this.id = id;
        this.fullTitle = fullTitle;
        this.singer = singer;
        this.title = title;
        this.size = size;
        this.length = length;
        this.dateTime = dateTime;
        this.date = date;
        this.time = time;
        this.byteOfPicture = byteOfPicture;
        this.albumTitle = albumTitle;
        this.year = year;
        this.genre = genre;
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

    public byte[] getByteOfPicture() {
        return byteOfPicture;
    }

    public void setByteOfPicture(byte[] byteOfPicture) {
        this.byteOfPicture = byteOfPicture;
    }

    public String getAlbumTitle() {
        return albumTitle;
    }

    public void setAlbumTitle(String albumTitle) {
        this.albumTitle = albumTitle;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public static Comparator<TrackDto> fullTitleTrackComparator = Comparator.comparing(TrackDto::getFullTitle);

    public static Comparator<TrackDto> lengthTrackComparator = Comparator.comparing(TrackDto::getLength);

    public static Comparator<TrackDto> sizeTrackComparator = (track1, track2) -> (int) (track1.getSize() - track2.getSize());

    public static Comparator<TrackDto> dateTrackComparator = Comparator.comparing(TrackDto::getDateTime);
}