package com.example.musicplayer.player.history.controller;

import com.example.musicplayer.player.history.service.HistoryService;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.sign.user.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @GetMapping("/count")
    public Integer getCountOfHistoryMusicByUserId(@AuthenticationPrincipal User user) {
        return historyService.getCountOfHistoryMusicByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<Object> setTrackToHistoryByUserId(@AuthenticationPrincipal User user, @RequestParam String fullTrackTitle) {
        if (user != null) {
            historyService.setTrackToHistoryByUserId(user.getId(), fullTrackTitle);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public Set<TrackDto> getHistoryTrackByUserId(@AuthenticationPrincipal User user) {
        return historyService.getHistoryByUserId(user.getId());
    }

    @DeleteMapping
    public String deleteTrackFromHistory(@AuthenticationPrincipal User user, int trackId) {
        return historyService.removeFromHistory(user.getId(), trackId);
    }
}