package com.example.musicplayer.player.history.controller;

import com.example.musicplayer.player.history.dto.HistoryTrackDto;
import com.example.musicplayer.player.history.service.HistoryService;
import com.example.musicplayer.player.history.service.HistoryTrackConverter;
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

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/history")
public class HistoryController {
    private final HistoryService historyService;
    private final HistoryTrackConverter converter;

    public HistoryController(HistoryService historyService,
                             HistoryTrackConverter converter) {
        this.historyService = historyService;
        this.converter = converter;
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
    public Set<HistoryTrackDto> getHistoryTrackByUserId(@AuthenticationPrincipal User user) {
        return historyService.getHistoryByUserId(user.getId()).stream()
                .map(converter::convertToHistoryTrackDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @DeleteMapping
    public String deleteTrackFromHistory(@AuthenticationPrincipal User user, int trackId) {
        return historyService.removeFromHistory(user.getId(), trackId);
    }
}