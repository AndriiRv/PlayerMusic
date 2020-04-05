package com.example.musicplayer.player.music.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TrackDto {
    private List<String> titles;
    private List<String> singers;
    private List<String> fullTitles;
    private List<String> lengths;
    private List<Double> sizes;
    private List<LocalDate> dates;
    private List<LocalTime> times;
    private List<LocalDateTime> datetimes;
    private List<String> albums;
    private List<String> years;
    private List<String> genres;

    public TrackDto(List<String> titles, List<String> singers, List<String> fullTitles,
                    List<String> lengths, List<Double> sizes, List<LocalDate> dates,
                    List<LocalTime> times, List<LocalDateTime> datetimes, List<String> albums,
                    List<String> years, List<String> genres) {
        this.titles = titles;
        this.singers = singers;
        this.fullTitles = fullTitles;
        this.lengths = lengths;
        this.sizes = sizes;
        this.dates = dates;
        this.times = times;
        this.datetimes = datetimes;
        this.albums = albums;
        this.years = years;
        this.genres = genres;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<String> getSingers() {
        return singers;
    }

    public void setSingers(List<String> singers) {
        this.singers = singers;
    }

    public List<String> getFullTitles() {
        return fullTitles;
    }

    public void setFullTitles(List<String> fullTitles) {
        this.fullTitles = fullTitles;
    }

    public List<String> getLengths() {
        return lengths;
    }

    public void setLengths(List<String> lengths) {
        this.lengths = lengths;
    }

    public List<Double> getSizes() {
        return sizes;
    }

    public void setSizes(List<Double> sizes) {
        this.sizes = sizes;
    }

    public List<LocalDate> getDates() {
        return dates;
    }

    public void setDates(List<LocalDate> dates) {
        this.dates = dates;
    }

    public List<LocalTime> getTimes() {
        return times;
    }

    public void setTimes(List<LocalTime> times) {
        this.times = times;
    }

    public List<LocalDateTime> getDatetimes() {
        return datetimes;
    }

    public void setDatetimes(List<LocalDateTime> datetimes) {
        this.datetimes = datetimes;
    }

    public List<String> getAlbums() {
        return albums;
    }

    public void setAlbums(List<String> albums) {
        this.albums = albums;
    }

    public List<String> getYears() {
        return years;
    }

    public void setYears(List<String> years) {
        this.years = years;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
}