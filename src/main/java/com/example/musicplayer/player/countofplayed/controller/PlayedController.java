package com.example.musicplayer.player.countofplayed.controller;

import com.example.musicplayer.player.countofplayed.service.PlayedService;
import com.example.musicplayer.player.music.service.MusicPlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/played")
public class PlayedController {
    private final MusicPlayerService musicPlayerService;
    private final PlayedService playedService;

    @Autowired
    public PlayedController(MusicPlayerService musicPlayerService,
                            PlayedService playedService) {
        this.musicPlayerService = musicPlayerService;
        this.playedService = playedService;
    }

    @PostMapping
    public void addCountOfPlayedByMusicTitle(int musicId) {
        musicPlayerService.addCountOfPlayedByMusicTitle(musicId);
    }

    @GetMapping
    public Integer getCountOfPlayedByMusicId(int musicId) {
        return playedService.getCountOfPlayedByMusicId(musicId);
    }
}