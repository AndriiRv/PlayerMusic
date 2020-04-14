package com.example.musicplayer.player.favourite.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.favourite.service.FavouriteService;
import com.example.musicplayer.player.music.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/favourite")
public class FavouriteController {
    private final FavouriteService favouriteService;

    @Autowired
    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @PostMapping
    public ResponseEntity<Object> setFavourite(@RequestParam String trackTitle, @AuthenticationPrincipal User user) {
        if (user != null) {
            boolean check = favouriteService.setMusicToFavourite(user.getId(), trackTitle);
            if (check) {
                favouriteService.deleteTrackFromFavourite(user.getId(), trackTitle);
                return new ResponseEntity<>("'" + trackTitle + "' - do not like you anymore", HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("'" + trackTitle + "' - added to your musictracks", HttpStatus.CREATED);
    }

    @PutMapping
    public void renameTrackByUser(@AuthenticationPrincipal User user, int trackId, String newTitleByUser) {
        favouriteService.renameTrackByUser(user, trackId, newTitleByUser);
    }

    @GetMapping
    public List<Track> getFavouriteByUser(@AuthenticationPrincipal User user) {
        List<Track> historyByUserId = favouriteService.getFavouriteTracksByUser(user.getId());
        if (!historyByUserId.isEmpty()) {
            return historyByUserId;
        }
        return new ArrayList<>();
    }

    @GetMapping("/already")
    public ResponseEntity<Object> isTrackAlreadyInFavouriteByUserId(@AuthenticationPrincipal User user, int musicId) {
        Integer ifExistCounter = favouriteService.isTrackAlreadyInFavouriteByUserId(user, musicId);
        return new ResponseEntity<>(ifExistCounter, HttpStatus.OK);
    }

    @GetMapping("/count")
    public ResponseEntity<Object> getCount(int musicId) {
        Integer counter = favouriteService.getCountOfFavouriteByMusicId(musicId);
        return new ResponseEntity<>(counter, HttpStatus.OK);
    }
}