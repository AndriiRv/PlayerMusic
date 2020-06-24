package com.example.musicplayer.player.search.globalsearch.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.search.dto.SearchTrackDto;
import org.springframework.stereotype.Component;

@Component
public class SearchTrackConverter {

    public SearchTrackDto convertToSearchTrackDto(Track track) {
        if (track == null) {
            return null;
        }
        SearchTrackDto searchTrackDto = new SearchTrackDto();
        searchTrackDto.setId(track.getId());
        searchTrackDto.setFullTitle(track.getFullTitle());
        searchTrackDto.setSinger(track.getSinger());
        searchTrackDto.setTitle(track.getTitle());
        searchTrackDto.setLength(track.getLength());
        searchTrackDto.setByteOfPicture(track.getByteOfPicture());
        searchTrackDto.setAlbumTitle(track.getAlbumTitle());
        searchTrackDto.setYear(track.getYear());
        searchTrackDto.setGenre(track.getGenre());
        return searchTrackDto;
    }

    public Track convertToTrack(SearchTrackDto searchTrackDto) {
        if (searchTrackDto == null) {
            return null;
        }
        Track track = new Track();
        track.setId(searchTrackDto.getId());
        track.setFullTitle(searchTrackDto.getFullTitle());
        track.setSinger(searchTrackDto.getSinger());
        track.setTitle(searchTrackDto.getTitle());
        track.setLength(searchTrackDto.getLength());
        track.setByteOfPicture(searchTrackDto.getByteOfPicture());
        track.setAlbumTitle(searchTrackDto.getAlbumTitle());
        track.setYear(searchTrackDto.getYear());
        track.setGenre(searchTrackDto.getGenre());
        return track;
    }
}