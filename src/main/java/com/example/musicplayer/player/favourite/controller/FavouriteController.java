package com.example.musicplayer.player.favourite.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.favourite.service.FavouriteService;
import com.example.musicplayer.player.music.model.Track;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
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
    @ResponseBody
    public void renameTrackByUser(@AuthenticationPrincipal User user, int trackId, String newTitleByUser) {
        favouriteService.renameTrackByUser(user, trackId, newTitleByUser);
    }

    @GetMapping
    @ResponseBody
    public List<Track> getFavouriteByUser(@AuthenticationPrincipal User user) {
        List<Track> historyByUserId = favouriteService.getFavouriteTracksByUser(user.getId());
        if (!historyByUserId.isEmpty()) {
            return historyByUserId;
        }
        return new ArrayList<>();
    }
}