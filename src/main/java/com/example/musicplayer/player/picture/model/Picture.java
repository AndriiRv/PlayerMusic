package com.example.musicplayer.player.picture.model;

public class Picture {
    private int musicId;
    private byte[] byteOfPicture;

    public int getMusicId() {
        return musicId;
    }

    public void setMusicId(int musicId) {
        this.musicId = musicId;
    }

    public byte[] getByteOfPicture() {
        return byteOfPicture;
    }

    public void setByteOfPicture(byte[] byteOfPicture) {
        this.byteOfPicture = byteOfPicture;
    }
}
