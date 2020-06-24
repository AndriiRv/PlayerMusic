package com.example.musicplayer.player.history.service;

import com.example.musicplayer.player.history.repository.HistoryRepository;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.service.MusicService;
import org.springframework.stereotype.Service;

import java.util.LinkedHashSet;
import java.util.Set;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final MusicService musicService;

    public HistoryService(HistoryRepository historyRepository,
                          MusicService musicService) {
        this.historyRepository = historyRepository;
        this.musicService = musicService;
    }

    public Integer getCountOfHistoryMusicByUserId(int userId) {
        return historyRepository.getCountOfHistoryMusicByUserId(userId);
    }

    public void setTrackToHistoryByUserId(int userId, String trackFullTitle) {
        Track track = musicService.getTrackByFullTitle(trackFullTitle);
        int trackAlreadyInHistoryByUserId = isTrackAlreadyInHistoryByUserId(userId, track.getId());
        if (trackAlreadyInHistoryByUserId == 0) {
            historyRepository.setTrackToHistoryByUserId(userId, track.getId());
        }
    }

    public Set<Track> getHistoryByUserId(int userId) {
        return new LinkedHashSet<>(historyRepository.getHistoryByUserId(userId));
    }

    private int isTrackAlreadyInHistoryByUserId(int userId, int musicId) {
        return historyRepository.isTrackAlreadyInHistoryByUserId(userId, musicId);
    }

    public String removeFromHistory(int userId, int trackId) {
        historyRepository.removeFromHistory(userId, trackId);
        return musicService.getFullTitleByMusicId(trackId);
    }
}
