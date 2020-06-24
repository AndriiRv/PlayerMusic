package com.example.musicplayer.player.uploadtrack.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.uploadtrack.dto.UploadedTrackDto;
import org.springframework.stereotype.Component;

@Component
public class UploadTrackConverter {

    public UploadedTrackDto convertToUploadedTrack(Track track) {
        if (track == null) {
            return null;
        }
        UploadedTrackDto uploadedTrackDto = new UploadedTrackDto();
        uploadedTrackDto.setId(track.getId());
        uploadedTrackDto.setFullTitle(track.getFullTitle());
        uploadedTrackDto.setByteOfPicture(track.getByteOfPicture());
        uploadedTrackDto.setLength(track.getLength());
        uploadedTrackDto.setDateTime(track.getDateTime());
        uploadedTrackDto.setAlbumTitle(track.getAlbumTitle());
        uploadedTrackDto.setYear(track.getYear());
        uploadedTrackDto.setGenre(track.getGenre());
        return uploadedTrackDto;
    }

    Track convertToTrack(UploadedTrackDto uploadedTrackDto) {
        if (uploadedTrackDto == null) {
            return null;
        }
        Track track = new Track();
        track.setId(uploadedTrackDto.getId());
        track.setFullTitle(uploadedTrackDto.getFullTitle());
        track.setLength(uploadedTrackDto.getLength());
        track.setDateTime(uploadedTrackDto.getDateTime());
        track.setByteOfPicture(uploadedTrackDto.getByteOfPicture());
        track.setAlbumTitle(uploadedTrackDto.getAlbumTitle());
        track.setYear(uploadedTrackDto.getYear());
        track.setGenre(uploadedTrackDto.getGenre());
        return track;
    }
}