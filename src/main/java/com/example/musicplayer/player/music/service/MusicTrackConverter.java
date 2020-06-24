package com.example.musicplayer.player.music.service;

import com.example.musicplayer.player.music.dto.TrackDto;
import com.example.musicplayer.player.music.model.Track;
import org.springframework.stereotype.Component;

@Component
public class MusicTrackConverter {

    public TrackDto convertToTrackDto(Track track) {
        if (track == null) {
            return null;
        }
        TrackDto trackDto = new TrackDto();
        trackDto.setId(track.getId());
        trackDto.setFullTitle(track.getFullTitle());
        trackDto.setByteOfPicture(track.getByteOfPicture());
        trackDto.setAlbumTitle(track.getAlbumTitle());
        return trackDto;
    }
}