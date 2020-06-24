package com.example.musicplayer.dashboard.service;

import com.example.musicplayer.dashboard.dto.DashboardDto;
import com.example.musicplayer.dashboard.dto.DashboardSideDto;
import com.example.musicplayer.player.favourite.service.FavouriteService;
import com.example.musicplayer.player.history.service.HistoryService;
import com.example.musicplayer.player.music.model.Track;
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
    private static final int MIN_SIZE_SOUNDTRACK_BY_GENRE = 5;
    private static final int MAX_SIZE_SOUNDTRACK_BY_GENRE = 15;
    private static final int MIN_SIZE_SOUNDTRACK_BY_FAV_AND_HIS = 3;

    private final FavouriteService favouriteService;
    private final HistoryService historyService;
    private final DashboardConverter converter;

    private final TrackList trackList;
    private Set<Track> tracks;

    public DashboardService(FavouriteService favouriteService,
                            HistoryService historyService,
                            DashboardConverter converter,
                            TrackList trackList) {
        this.favouriteService = favouriteService;
        this.historyService = historyService;
        this.converter = converter;
        this.trackList = trackList;
    }

    public void getMusicForDashboard() {
        Set<Track> musicForDashboard = trackList.getMusicTracks();
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
                .map(Track::getGenre)
                .filter(Objects::nonNull)
                .filter(genre -> getMusicTrackByGenreTitle(genre).size() >= MIN_SIZE_SOUNDTRACK_BY_GENRE)
                .collect(Collectors.toSet());
    }

    public Set<DashboardDto> getMusicTrackByGenreTitle(String genre) {
        Set<DashboardDto> collect = tracks.stream()
                .filter(e -> e.getGenre() != null && e.getGenre().equals(genre))
                .limit(MAX_SIZE_SOUNDTRACK_BY_GENRE)
                .map(converter::convertToGenreTrackDto)
                .collect(Collectors.toSet());

        return collect.size() >= MIN_SIZE_SOUNDTRACK_BY_GENRE ? collect : new HashSet<>();
    }

    public Set<DashboardDto> getMusicByCountOfPlayed(int page) {
        return tracks.stream()
                .sorted(Comparator.comparing(Track::getCountOfPlayed).reversed())
                .limit(page)
                .map(converter::convertToGenreTrackDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<DashboardDto> getMusicByCountOfFavourite(int page) {
        return tracks.stream()
                .sorted(Comparator.comparing(Track::getCountOfFavourite).reversed())
                .limit(page)
                .map(converter::convertToGenreTrackDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<DashboardSideDto> getFavouriteMusicByUserId(int userId) {
        return favouriteService.getFavouriteTracksByUserId(userId).stream()
                .limit(MIN_SIZE_SOUNDTRACK_BY_FAV_AND_HIS)
                .map(converter::convertToDashboardSideDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public Set<DashboardSideDto> getHistoryMusicByUserId(int userId) {
        Set<DashboardSideDto> list = historyService.getHistoryByUserId(userId).stream()
                .limit(MIN_SIZE_SOUNDTRACK_BY_FAV_AND_HIS)
                .map(converter::convertToDashboardSideDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));

        list.forEach(e -> {
            if (e.getCountOfPlayed() == null) {
                e.setCountOfPlayed(0);
            }
            if (e.getCountOfFavourite() == null) {
                e.setCountOfFavourite(0);
            }
        });
        return list;
    }
}