package com.example.musicplayer.player.picture.controller;

import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.picture.service.PictureService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PictureController {
    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @GetMapping(value = "/picture")
    public TrackDto getPictureByMusicId(@RequestParam Integer musicId) {
        return pictureService.getPictureByMusicId(musicId);
    }
}
