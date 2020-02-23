package com.example.musicplayer.player.service;

import com.example.musicplayer.player.model.Track;
import com.example.musicplayer.player.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryService {
    private final HistoryRepository historyRepository;
    private final MusicService musicService;

    @Autowired
    public HistoryService(HistoryRepository historyRepository,
                          MusicService musicService) {
        this.historyRepository = historyRepository;
        this.musicService = musicService;
    }

    public void setTrackToHistoryByUserId(int userId, String trackFullTitle) {
        Track track = musicService.getTrackByFullTitle(trackFullTitle);
        int trackAlreadyInHistoryByUserId = isTrackAlreadyInHistoryByUserId(userId, track.getId());
        if (trackAlreadyInHistoryByUserId == 0) {
            historyRepository.setTrackToHistoryByUserId(userId, track.getId());
        }
    }

    public List<String> getHistoryByUserId(int userId) {
        return historyRepository.getHistoryByUserId(userId);
    }

    private int isTrackAlreadyInHistoryByUserId(int userId, int musicId) {
        return historyRepository.isTrackAlreadyInHistoryByUserId(userId, musicId);
    }

    public void removeFromHistory(int userId, String trackFullTitle) {
        Track track = musicService.getTrackByFullTitle(trackFullTitle);
        historyRepository.removeFromHistory(userId, track.getId());
    }
}
