package com.example.musicplayer.player.search.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.model.TrackDto;
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

    public Set<TrackDto> searchTracks(String searchString) {
        if (!searchString.isBlank()) {
            Set<TrackDto> searchableTracks = new HashSet<>();

            for (TrackDto element : musicPlayerService.getListOfTracks()) {
                if (element.getFullTitle().toLowerCase().contains(searchString)) {
                    searchableTracks.add(element);
                } else if (element.getSinger().toLowerCase().contains(searchString)) {
                    searchableTracks.add(element);
                } else if (element.getGenre() != null && element.getGenre().toLowerCase().contains(searchString)) {
                    searchableTracks.add(element);
                } else if (element.getYear() != null && element.getYear().contains(searchString)) {
                    searchableTracks.add(element);
                } else if (element.getSinger().contains(searchString)) {
                    searchableTracks.add(element);
                }
            }
            return searchableTracks;
        }
        return new HashSet<>();
    }
}