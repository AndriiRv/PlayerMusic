package com.example.musicplayer.player.countofplayed.service;

import com.example.musicplayer.player.countofplayed.repository.PlayedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlayedService {
    private final PlayedRepository playedRepository;

    @Autowired
    public PlayedService(PlayedRepository playedRepository) {
        this.playedRepository = playedRepository;
    }

    public Integer getCountOfPlayedByMusicId(int musicId) {
        return playedRepository.getCountOfPlayedByMusicId(musicId);
    }

    public void addCountOfPlayedByMusicId(int musicId) {
        playedRepository.addCountOfPlayedByMusicId(musicId);
    }
}