package com.example.musicplayer.player.music.model;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;

public class Track {
    private Integer id;
    private String fullTitle;
    private String singer;
    private String title;
    private double size;
    private String length;
    private LocalDateTime dateTime;
    private byte[] byteOfPicture;
    private String albumTitle;
    private String year;
    private String genre;
    private Integer countOfPlayed;
    private Integer countOfFavourite;

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

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Track track = (Track) o;
        return Double.compare(track.size, size) == 0 &&
                Objects.equals(id, track.id) &&
                Objects.equals(fullTitle, track.fullTitle) &&
                Objects.equals(singer, track.singer) &&
                Objects.equals(title, track.title) &&
                Objects.equals(length, track.length) &&
                Objects.equals(dateTime, track.dateTime) &&
                Arrays.equals(byteOfPicture, track.byteOfPicture) &&
                Objects.equals(albumTitle, track.albumTitle) &&
                Objects.equals(year, track.year) &&
                Objects.equals(genre, track.genre);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, fullTitle, singer, title, size, length, dateTime, albumTitle, year, genre);
        result = 31 * result + Arrays.hashCode(byteOfPicture);
        return result;
    }
}