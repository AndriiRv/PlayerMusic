package com.example.musicplayer.player.search.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SearchService {
    private final MusicPlayerService musicPlayerService;

    @Autowired
    public SearchService(MusicPlayerService musicPlayerService) {
        this.musicPlayerService = musicPlayerService;
    }

    public Set<Track> searchTracks(String searchString) {
        Set<Track> searchableTracks = new HashSet<>();

        for (Track element : musicPlayerService.getListOfTracks()) {
            if (element.getFullTitle().toLowerCase().contains(searchString)) {
                searchableTracks.add(element);
            }
        }
        return searchableTracks;
    }
}