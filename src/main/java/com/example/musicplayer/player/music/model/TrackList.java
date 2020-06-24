package com.example.musicplayer.player.music.model;

import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class TrackList {
    private Set<Track> musicTracks;

    public Set<Track> getMusicTracks() {
        return musicTracks;
    }

    public void setMusicTracks(Set<Track> musicTracks) {
        this.musicTracks = musicTracks;
    }
}