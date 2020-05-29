package com.example.musicplayer.player.picture.service;

import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.picture.model.Picture;
import com.example.musicplayer.player.picture.repository.PictureRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureService {
    private final PictureRepository pictureRepository;

    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public void addPictureToMusic(int musicId, byte[] picture) {
        pictureRepository.addPictureToMusic(musicId, picture);
    }

    public TrackDto getPictureByMusicId(int musicId) {
        TrackDto trackDto = new TrackDto();
        trackDto.setByteOfPicture(pictureRepository.getPictureByMusicId(musicId));
        return trackDto;
    }

    public List<Picture> getAllPicture() {
        return pictureRepository.getAllPicture();
    }
}
