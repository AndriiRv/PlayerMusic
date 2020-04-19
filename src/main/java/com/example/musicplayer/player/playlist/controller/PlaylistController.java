package com.example.musicplayer.player.playlist.controller;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.playlist.model.Playlist;
import com.example.musicplayer.player.playlist.service.PlaylistService;
import com.example.musicplayer.sign.authentication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

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
    public ResponseEntity<Object> createPlaylist(@AuthenticationPrincipal User user, @RequestParam String titleOfPlaylist) {
        boolean isOk = playlistService.createPlaylist(user.getId(), titleOfPlaylist);
        if (isOk) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @GetMapping
    @ResponseBody
    public List<Playlist> getAllPlaylistsByUser(@AuthenticationPrincipal User user) {
        return playlistService.getAllPlaylistsByUser(user.getId());
    }

    @PostMapping("/addMusic")
    @ResponseStatus(HttpStatus.OK)
    public void insertMusicTrackToPlaylistByUser(@AuthenticationPrincipal User user,
                                                 @RequestParam String titleOfPlaylist,
                                                 @RequestParam String trackTitle) {
        playlistService.insertMusicTrackToPlaylistByUser(user.getId(), titleOfPlaylist, trackTitle);
    }

    @GetMapping("/music")
    @ResponseBody
    public List<Track> getMusicFromPlaylistByUser(@AuthenticationPrincipal User user, @RequestParam String titleOfPlaylist) {
        return playlistService.getMusicFromPlaylistByUser(user.getId(), titleOfPlaylist);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deletePlaylist(@AuthenticationPrincipal User user, @RequestParam Integer playlistId) {
        playlistService.deletePlaylist(user.getId(), playlistId);
    }

    @DeleteMapping("/music")
    @ResponseStatus(HttpStatus.OK)
    public void deleteTrackPlaylist(@AuthenticationPrincipal User user, @RequestParam String playlistTitle, @RequestParam Integer trackId) {
        playlistService.deleteTrackFromPlaylist(user.getId(), playlistTitle, trackId);
    }
}