package com.example.musicplayer.player.history.service;

import com.example.musicplayer.player.history.dto.HistoryTrackDto;
import com.example.musicplayer.player.music.model.Track;
import org.springframework.stereotype.Component;

@Component
public class HistoryTrackConverter {

    public HistoryTrackDto convertToHistoryTrackDto(Track track) {
        HistoryTrackDto dto = new HistoryTrackDto();
        dto.setId(track.getId());
        dto.setFullTitle(track.getFullTitle());
        dto.setByteOfPicture(track.getByteOfPicture());
        return dto;
    }
}