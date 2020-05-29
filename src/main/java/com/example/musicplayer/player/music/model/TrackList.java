package com.example.musicplayer.player.music.model;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TrackList {
    private Map<Integer, TrackDto> musicTracks;

    public Map<Integer, TrackDto> getMusicTracks() {
        return musicTracks;
    }

    public void setMusicTracks(Map<Integer, TrackDto> musicTracks) {
        this.musicTracks = musicTracks;
    }
}