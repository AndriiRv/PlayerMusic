package com.example.musicplayer.dashboard.controller;

import com.example.musicplayer.dashboard.dto.DashboardDto;
import com.example.musicplayer.dashboard.dto.DashboardSideDto;
import com.example.musicplayer.dashboard.service.DashboardService;
import com.example.musicplayer.sign.user.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public Set<DashboardDto> getMusicTrackByGenreTitle(@RequestParam String genre) {
        return dashboardService.getMusicTrackByGenreTitle(genre);
    }

    @GetMapping("/countOfPlayed")
    public Set<DashboardDto> getMusicByCountOfPlayed(@RequestParam Integer page) {
        return dashboardService.getMusicByCountOfPlayed(page);
    }

    @GetMapping("/countOfFavourite")
    public Set<DashboardDto> getMusicByCountOfFavourite(@RequestParam Integer page) {
        return dashboardService.getMusicByCountOfFavourite(page);
    }

    @GetMapping("/favourite")
    public Set<DashboardSideDto> getFavouriteByUser(@AuthenticationPrincipal User user) {
        return dashboardService.getFavouriteMusicByUserId(user.getId());
    }

    @GetMapping("/history")
    public Set<DashboardSideDto> getHistoryByUser(@AuthenticationPrincipal User user) {
        return dashboardService.getHistoryMusicByUserId(user.getId());
    }
}