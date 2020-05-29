package com.example.musicplayer.player.statistic.played.controller;

import com.example.musicplayer.player.statistic.played.service.StatisticPlayedService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistic/played")
public class StatisticPlayedController {
    private final StatisticPlayedService statisticPlayedService;

    public StatisticPlayedController(StatisticPlayedService statisticPlayedService) {
        this.statisticPlayedService = statisticPlayedService;
    }

    @GetMapping
    public Integer getCountOfPlayedByMusicId(int musicId) {
        return statisticPlayedService.getCountOfPlayedByMusicId(musicId);
    }

    @PostMapping
    public void addCountOfPlayedByMusicTitle(int musicId) {
        statisticPlayedService.addCountOfPlayedByMusicId(musicId);
    }
}