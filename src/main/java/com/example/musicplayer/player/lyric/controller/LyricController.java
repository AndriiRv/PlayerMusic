package com.example.musicplayer.player.lyric.controller;

import com.example.musicplayer.player.lyric.model.Lyric;
import com.example.musicplayer.player.lyric.service.LyricService;
import com.example.musicplayer.sign.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LyricController {
    private final LyricService lyricService;

    public LyricController(LyricService lyricService) {
        this.lyricService = lyricService;
    }

    @GetMapping("/lyric")
    public ResponseEntity<Object> getLyric(@AuthenticationPrincipal User user, String nameOfTrack, String artistOfTrack) {
        Lyric lyric = lyricService.getLyric(user, nameOfTrack, artistOfTrack);
        if (lyric.isLyricFoundByApi()) {
            return new ResponseEntity<>(lyric.getLyricText(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Lyric of track not found via API", HttpStatus.CONFLICT);
        }
    }
}