package com.example.musicplayer.player.picture.controller;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.service.MusicService;
import com.example.musicplayer.player.picture.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PictureController {
    private final PictureService pictureService;
    private final MusicService musicService;

    @Autowired
    public PictureController(PictureService pictureService,
                             MusicService musicService) {
        this.pictureService = pictureService;
        this.musicService = musicService;
    }

    @GetMapping(value = "/picture", produces = {MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] getPictureByMusicId(@RequestParam String musicTitle) {
        Track track = musicService.getTrackByFullTitle(musicTitle);
        return pictureService.getPictureByMusicId(track.getId());
    }

//    @GetMapping(value = "/allPicture", produces = {MediaType.IMAGE_PNG_VALUE})
//    @ResponseBody
//    public List<Picture> getAllPicture(@RequestParam String musicTitle) {
//        return pictureService.getAllPicture();
//    }
}
