package com.example.musicplayer.player.controller;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;

    @Autowired
    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity createPlaylist(@AuthenticationPrincipal User user, @RequestParam String titleOfPlaylist) {
        if (user != null) {
            playlistService.createPlaylist(user.getId(), titleOfPlaylist);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping
    @ResponseBody
    public List<String> getAllPlaylistsByUser(@AuthenticationPrincipal User user) {
        if (user != null) {
            return playlistService.getAllPlaylistsByUser(user.getId());
        }
        return new ArrayList<>();
    }

    @PostMapping("/addMusic")
    public ResponseEntity insertMusicTrackToPlaylistByUser(@AuthenticationPrincipal User user,
                                                           @RequestParam String titleOfPlaylist,
                                                           @RequestParam String trackTitle) {
        if (user != null) {
            playlistService.insertMusicTrackToPlaylistByUser(user.getId(), titleOfPlaylist, trackTitle);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/playlistMusic")
    @ResponseBody
    public List<String> getMusicFromPlaylistByUser(@AuthenticationPrincipal User user, @RequestParam String titleOfPlaylist) {
        if (user != null) {
            return playlistService.getMusicFromPlaylistByUser(user.getId(), titleOfPlaylist);
        }
        return new ArrayList<>();
    }
}
