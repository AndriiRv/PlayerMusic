package com.example.musicplayer.dashboard.controller;

import com.example.musicplayer.dashboard.service.DashboardService;
import com.example.musicplayer.player.music.model.TrackDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/genre")
    public Set<String> getAllGenre() {
        return dashboardService.getAllGenre();
    }

    @GetMapping("/byGenre")
    public Set<TrackDto> getMusicTrackByGenreTitle(@RequestParam String genre) {
        return dashboardService.getMusicTrackByGenreTitle(genre);
    }

    @GetMapping("/countOfPlayed")
    public Set<TrackDto> getMusicByCountOfPlayed(@RequestParam Integer page) {
        return dashboardService.getMusicByCountOfPlayed(page);
    }

    @GetMapping("/countOfFavourite")
    public Set<TrackDto> getMusicByCountOfFavourite(@RequestParam Integer page) {
        return dashboardService.getMusicByCountOfFavourite(page);
    }
}