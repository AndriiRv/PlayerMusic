package com.example.musicplayer.player.history.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.history.service.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;

    @Autowired
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }

    @PostMapping
    public ResponseEntity<Object> setTrackToHistoryByUserId(@AuthenticationPrincipal User user, @RequestParam String fullTrackTitle) {
        if (user != null) {
            historyService.setTrackToHistoryByUserId(user.getId(), fullTrackTitle);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public List<Track> getHistoryTrackByUserId(@AuthenticationPrincipal User user) {
        List<Track> historyByUserId = historyService.getHistoryByUserId(user.getId());
        if (!historyByUserId.isEmpty()) {
            return historyByUserId;
        }
        return new ArrayList<>();
    }

    @DeleteMapping
    @ResponseBody
    public String deleteTrackFromHistory(@AuthenticationPrincipal User user, int trackId) {
        return historyService.removeFromHistory(user.getId(), trackId);
    }
}