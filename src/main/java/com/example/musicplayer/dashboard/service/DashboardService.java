package com.example.musicplayer.dashboard.service;

import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.music.model.TrackList;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DashboardService {
    private final TrackList trackList;
    private Set<TrackDto> tracks;

    public DashboardService(TrackList trackList) {
        this.trackList = trackList;
    }

    public void getMusicForDashboard() {
        Set<TrackDto> musicForDashboard = new HashSet<>(trackList.getMusicTracks().values());
        musicForDashboard.forEach(e -> {
            if (e.getCountOfPlayed() == null) {
                e.setCountOfPlayed(0);
            }
            if (e.getCountOfFavourite() == null) {
                e.setCountOfFavourite(0);
            }
        });
        tracks = musicForDashboard;
    }

    public Set<String> getAllGenre() {
        return tracks.stream()
                .map(TrackDto::getGenre)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    public Set<TrackDto> getMusicTrackByGenreTitle(String genre) {
        Set<TrackDto> collect = tracks.stream()
                .filter(e -> e.getGenre() != null && e.getGenre().equals(genre))
                .collect(Collectors.toSet());

        if (collect.size() >= 5) {
            return collect;
        }
        return new HashSet<>();
    }

    public Set<TrackDto> getMusicByCountOfPlayed(int page) {
        return tracks.stream()
                .sorted(Comparator.comparing(TrackDto::getCountOfPlayed).reversed())
                .limit(page)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<TrackDto> getMusicByCountOfFavourite(int page) {
        return tracks.stream()
                .sorted(Comparator.comparing(TrackDto::getCountOfFavourite).reversed())
                .limit(page)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}