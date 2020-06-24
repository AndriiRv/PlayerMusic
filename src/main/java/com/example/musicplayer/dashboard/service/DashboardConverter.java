package com.example.musicplayer.dashboard.service;

import com.example.musicplayer.dashboard.dto.DashboardDto;
import com.example.musicplayer.dashboard.dto.DashboardSideDto;
import com.example.musicplayer.player.music.model.Track;
import org.springframework.stereotype.Component;

@Component
class DashboardConverter {

    DashboardDto convertToGenreTrackDto(Track track) {
        DashboardDto dto = new DashboardDto();
        dto.setId(track.getId());
        dto.setFullTitle(track.getFullTitle());
        dto.setByteOfPicture(track.getByteOfPicture());
        return dto;
    }

    DashboardSideDto convertToDashboardSideDto(Track track) {
        DashboardSideDto dto = new DashboardSideDto();
        dto.setId(track.getId());
        dto.setFullTitle(track.getFullTitle());
        dto.setByteOfPicture(track.getByteOfPicture());
        dto.setCountOfFavourite(track.getCountOfFavourite());
        dto.setCountOfPlayed(track.getCountOfPlayed());
        return dto;
    }
}