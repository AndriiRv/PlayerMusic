package com.example.musicplayer.player.search.globalsearch.service;

import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.music.model.TrackList;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final TrackList trackList;

    public SearchService(TrackList trackList) {
        this.trackList = trackList;
    }

    public Set<TrackDto> searchTracks(String searchString) {
        if (!searchString.isBlank()) {
            return trackList.getMusicTracks().values().stream()
                    .filter(track -> track.getSinger().toLowerCase().contains(searchString)
                            || track.getGenre() != null && track.getGenre().toLowerCase().contains(searchString)
                            || track.getYear() != null && track.getYear().contains(searchString)
                            || track.getTitle().toLowerCase().contains(searchString))
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}