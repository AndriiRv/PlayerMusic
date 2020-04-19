package com.example.musicplayer.player.lyric.controller;

import com.example.musicplayer.player.lyric.model.Lyric;
import com.example.musicplayer.player.lyric.service.LyricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LyricController {
    private final LyricService lyricService;

    @Autowired
    public LyricController(LyricService lyricService) {
        this.lyricService = lyricService;
    }

    @GetMapping("/lyric")
    public ResponseEntity<Object> getLyric(String nameOfTrack, String artistOfTrack) {
        Lyric lyric = lyricService.getLyric(nameOfTrack, artistOfTrack);
        if (lyric.isLyricFoundByApi()) {
            return new ResponseEntity<>(lyric.getLyricText(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Lyric of track not found via API", HttpStatus.CONFLICT);
        }
    }
}