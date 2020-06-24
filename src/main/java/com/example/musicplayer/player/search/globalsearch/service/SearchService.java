package com.example.musicplayer.player.search.globalsearch.service;

import com.example.musicplayer.player.music.model.TrackList;
import com.example.musicplayer.player.search.dto.SearchTrackDto;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchService {
    private final TrackList trackList;
    private final SearchTrackConverter converter;

    public SearchService(TrackList trackList,
                         SearchTrackConverter converter) {
        this.trackList = trackList;
        this.converter = converter;
    }

    public Set<SearchTrackDto> searchTracks(String searchString) {
        if (!searchString.isBlank()) {
            return trackList.getMusicTracks().stream()
                    .filter(track -> track.getSinger().toLowerCase().contains(searchString)
                            || track.getGenre() != null && track.getGenre().toLowerCase().contains(searchString)
                            || track.getYear() != null && track.getYear().contains(searchString)
                            || track.getTitle().toLowerCase().contains(searchString))
                    .map(converter::convertToSearchTrackDto)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
        return new HashSet<>();
    }
}