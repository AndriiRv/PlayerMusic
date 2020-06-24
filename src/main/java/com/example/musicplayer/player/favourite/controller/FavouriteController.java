package com.example.musicplayer.player.favourite.controller;

import com.example.musicplayer.player.favourite.dto.FavouriteTrackDto;
import com.example.musicplayer.player.favourite.service.FavouriteService;
import com.example.musicplayer.player.favourite.service.FavouriteTrackConverter;
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

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/favourite")
public class FavouriteController {
    private final FavouriteService favouriteService;
    private final FavouriteTrackConverter converter;

    public FavouriteController(FavouriteService favouriteService,
                               FavouriteTrackConverter converter) {
        this.favouriteService = favouriteService;
        this.converter = converter;
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
                return new ResponseEntity<>("'" + fullTitle + "'\ndo not like you anymore", HttpStatus.OK);
            }
            return new ResponseEntity<>("'" + fullTitle + "'\nadded to your favourite", HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public void renameTrackByUser(@AuthenticationPrincipal User user, int trackId, String newTitleByUser) {
        favouriteService.renameTrackByUser(user, trackId, newTitleByUser);
    }

    @GetMapping
    public Set<FavouriteTrackDto> getFavouriteByUser(@AuthenticationPrincipal User user) {
        return favouriteService.getFavouriteTracksByUserId(user.getId()).stream()
                .map(converter::convertToFavouriteTrackDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @GetMapping("/already")
    public ResponseEntity<Object> isTrackAlreadyInFavouriteByUserId(@AuthenticationPrincipal User user, int musicId) {
        Integer ifExistCounter = favouriteService.isTrackAlreadyInFavouriteByUserId(user, musicId);
        return new ResponseEntity<>(ifExistCounter, HttpStatus.OK);
    }
}