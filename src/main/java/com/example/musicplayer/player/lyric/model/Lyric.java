package com.example.musicplayer.player.lyric.model;

public class Lyric {
    private String lyricText;
    private boolean isLyricFoundByApi;

    public Lyric(String lyricText, boolean isLyricFoundByApi) {
        this.lyricText = lyricText;
        this.isLyricFoundByApi = isLyricFoundByApi;
    }

    public String getLyricText() {
        return lyricText;
    }

    public void setLyricText(String lyricText) {
        this.lyricText = lyricText;
    }

    public boolean isLyricFoundByApi() {
        return isLyricFoundByApi;
    }

    public void setLyricFoundByApi(boolean lyricFoundByApi) {
        isLyricFoundByApi = lyricFoundByApi;
    }
}