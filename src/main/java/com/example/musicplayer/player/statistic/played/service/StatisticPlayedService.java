package com.example.musicplayer.player.statistic.played.service;

import com.example.musicplayer.player.statistic.played.repository.StatisticPlayedRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StatisticPlayedService {
    private final StatisticPlayedRepository statisticPlayedRepository;

    public StatisticPlayedService(StatisticPlayedRepository statisticPlayedRepository) {
        this.statisticPlayedRepository = statisticPlayedRepository;
    }

    public List<Integer> getCountOfPlayedByMusicIds(List<Integer> musicTrackIds) {
        return statisticPlayedRepository.getCountOfPlayedByMusicIds(musicTrackIds);
    }

    public Integer getCountOfPlayedByMusicId(int musicId) {
        return Objects.requireNonNullElse(statisticPlayedRepository.getCountOfPlayedByMusicId(musicId), 0);
    }

    public void addCountOfPlayedByMusicId(int musicId) {
        statisticPlayedRepository.addCountOfPlayedByMusicId(musicId);
    }
}