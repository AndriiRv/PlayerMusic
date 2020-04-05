package com.example.musicplayer.player.picture.service;

import com.example.musicplayer.player.picture.model.Picture;
import com.example.musicplayer.player.picture.repository.PictureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PictureService {
    private final PictureRepository pictureRepository;

    @Autowired
    public PictureService(PictureRepository pictureRepository) {
        this.pictureRepository = pictureRepository;
    }

    public void addPictureToMusic(int musicId, byte[] picture) {
        pictureRepository.addPictureToMusic(musicId, picture);
    }

    public byte[] getPictureByMusicId(int musicId) {
        return pictureRepository.getPictureByMusicId(musicId);
    }

    public List<Picture> getAllPicture() {
        return pictureRepository.getAllPicture();
    }
}
