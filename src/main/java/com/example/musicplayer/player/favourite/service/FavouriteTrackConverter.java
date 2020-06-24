package com.example.musicplayer.player.favourite.service;

import com.example.musicplayer.player.favourite.dto.FavouriteTrackDto;
import com.example.musicplayer.player.music.model.Track;
import org.springframework.stereotype.Component;

@Component
public class FavouriteTrackConverter {

    public FavouriteTrackDto convertToFavouriteTrackDto(Track track) {
        FavouriteTrackDto dto = new FavouriteTrackDto();
        dto.setId(track.getId());
        dto.setFullTitle(track.getFullTitle());
        dto.setByteOfPicture(track.getByteOfPicture());
        return dto;
    }
}