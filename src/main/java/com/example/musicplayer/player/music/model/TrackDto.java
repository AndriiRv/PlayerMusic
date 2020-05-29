package com.example.musicplayer.player.music.model;

import java.time.LocalDateTime;

public class TrackDto {
    private Integer id;
    private String fullTitle;
    private String singer;
    private String title;
    private Double size;
    private String length;
    private LocalDateTime dateTime;
    private byte[] byteOfPicture;
    private String albumTitle;
    private String year;
    private String genre;
    private Integer countOfPlayed;
    private Integer countOfFavourite;

    public static TrackDto of(Track track) {
        TrackDto trackDto = new TrackDto();
        trackDto.setId(track.getId());
        trackDto.setFullTitle(track.getFullTitle());
        trackDto.setSinger(track.getSinger());
        trackDto.setTitle(track.getTitle());
        trackDto.setSize(track.getSize());
        trackDto.setLength(track.getLength());
        trackDto.setDateTime(track.getDateTime());
        trackDto.setByteOfPicture(track.getByteOfPicture());
        trackDto.setAlbumTitle(track.getAlbumTitle());
        trackDto.setYear(track.getYear());
        trackDto.setGenre(track.getGenre());
        trackDto.setCountOfPlayed(track.getCountOfPlayed());
        trackDto.setCountOfFavourite(track.getCountOfFavourite());
        return trackDto;
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

    public Integer getCountOfPlayed() {
        return countOfPlayed;
    }

    public void setCountOfPlayed(Integer countOfPlayed) {
        this.countOfPlayed = countOfPlayed;
    }

    public Integer getCountOfFavourite() {
        return countOfFavourite;
    }

    public void setCountOfFavourite(Integer countOfFavourite) {
        this.countOfFavourite = countOfFavourite;
    }
}