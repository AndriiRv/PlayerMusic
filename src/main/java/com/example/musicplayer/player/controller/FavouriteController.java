package com.example.musicplayer.player.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.service.FavouriteService;
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
    public ResponseEntity setFavourite(@RequestParam String trackTitle, @AuthenticationPrincipal User user) {
        if (user != null) {
            favouriteService.setMusicToFavourite(user.getId(), trackTitle);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public List<String> favourite(@AuthenticationPrincipal User user) {
        if (user != null) {
            return favouriteService.getFavouriteTracks(user.getId());
        }
        return new ArrayList<>();
    }

    @DeleteMapping
    public ResponseEntity deleteFromFavourite(@AuthenticationPrincipal User user, @RequestParam String trackTitle) {
        if (user != null) {
            favouriteService.deleteTrackFromFavourite(user.getId(), trackTitle);
        }
        return new ResponseEntity(HttpStatus.OK);
    }
}
