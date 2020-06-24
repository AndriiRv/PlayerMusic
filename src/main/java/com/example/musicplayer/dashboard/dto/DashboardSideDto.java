package com.example.musicplayer.dashboard.dto;

public class DashboardSideDto {
    private Integer id;
    private String fullTitle;
    private byte[] byteOfPicture;
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

    public byte[] getByteOfPicture() {
        return byteOfPicture;
    }

    public void setByteOfPicture(byte[] byteOfPicture) {
        this.byteOfPicture = byteOfPicture;
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