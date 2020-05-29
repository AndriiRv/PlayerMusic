package com.example.musicplayer.player.favourite.controller;

import com.example.musicplayer.player.favourite.service.FavouriteService;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.sign.user.model.User;
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
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/favourite")
public class FavouriteController {
    private final FavouriteService favouriteService;

    public FavouriteController(FavouriteService favouriteService) {
        this.favouriteService = favouriteService;
    }

    @GetMapping("/count")
    public Integer getCountOfFavouriteMusicByUserId(@AuthenticationPrincipal User user) {
        return favouriteService.getCountOfFavouriteMusicByUserId(user.getId());
    }

    @PostMapping
    public ResponseEntity<Object> addFavourite(@RequestParam Integer trackId, @AuthenticationPrincipal User user) {
        if (user != null) {
            Map<String, Boolean> map = favouriteService.setMusicToFavourite(user.getId(), trackId);
            String fullTitle = map.keySet().stream().findFirst().orElse("");

            if (map.values().stream().findFirst().orElse(false)) {
                favouriteService.deleteTrackFromFavourite(user.getId(), trackId);
                return new ResponseEntity<>("'" + fullTitle + "' - do not like you anymore", HttpStatus.OK);
            }
            return new ResponseEntity<>("'" + fullTitle + "' - added to your favourite", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public void renameTrackByUser(@AuthenticationPrincipal User user, int trackId, String newTitleByUser) {
        favouriteService.renameTrackByUser(user, trackId, newTitleByUser);
    }

    @GetMapping
    public Set<TrackDto> getFavouriteByUser(@AuthenticationPrincipal User user) {
        return favouriteService.getFavouriteTracksByUser(user.getId());
    }

    @GetMapping("/already")
    public ResponseEntity<Object> isTrackAlreadyInFavouriteByUserId(@AuthenticationPrincipal User user, int musicId) {
        Integer ifExistCounter = favouriteService.isTrackAlreadyInFavouriteByUserId(user, musicId);
        return new ResponseEntity<>(ifExistCounter, HttpStatus.OK);
    }
}