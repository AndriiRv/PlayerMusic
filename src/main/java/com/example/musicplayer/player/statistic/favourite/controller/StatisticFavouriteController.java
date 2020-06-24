package com.example.musicplayer.player.statistic.favourite.controller;

import com.example.musicplayer.player.statistic.favourite.service.StatisticFavouriteService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/statistic/favourite")
public class StatisticFavouriteController {
    private final StatisticFavouriteService statisticFavouriteService;

    public StatisticFavouriteController(StatisticFavouriteService statisticFavouriteService) {
        this.statisticFavouriteService = statisticFavouriteService;
    }

    @GetMapping
    public Integer getCountOfFavouriteByMusicId(int musicId) {
        return statisticFavouriteService.getCountOfFavouriteByMusicId(musicId);
    }
}