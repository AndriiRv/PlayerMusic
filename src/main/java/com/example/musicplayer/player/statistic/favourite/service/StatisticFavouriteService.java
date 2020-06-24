package com.example.musicplayer.player.statistic.favourite.service;

import com.example.musicplayer.player.statistic.favourite.repository.StatisticFavouriteRepository;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class StatisticFavouriteService {
    private final StatisticFavouriteRepository statisticFavouriteRepository;

    public StatisticFavouriteService(StatisticFavouriteRepository statisticFavouriteRepository) {
        this.statisticFavouriteRepository = statisticFavouriteRepository;
    }

    public Integer getCountOfFavouriteByMusicId(int musicId) {
        return Objects.requireNonNullElse(statisticFavouriteRepository.getCountOfFavouriteByMusicId(musicId), 0);
    }

    public void addCountOfFavouriteByMusicId(int musicId) {
        statisticFavouriteRepository.addCountOfFavouriteByMusicId(musicId);
    }
}