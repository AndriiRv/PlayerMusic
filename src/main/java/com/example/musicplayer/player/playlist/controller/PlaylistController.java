package com.example.musicplayer.player.playlist.controller;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.playlist.model.Playlist;
import com.example.musicplayer.player.playlist.service.PlaylistService;
import com.example.musicplayer.sign.user.model.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {
    private final PlaylistService playlistService;

    public PlaylistController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @PostMapping
    public void createPlaylist(@AuthenticationPrincipal User user, String titleOfPlaylist) {
        playlistService.createPlaylist(user.getId(), titleOfPlaylist);
    }

    @GetMapping
    public List<Playlist> getAllPlaylistsByUser(@AuthenticationPrincipal User user) {
        return playlistService.getAllPlaylistsByUser(user.getId());
    }

    @PostMapping("/addMusic")
    public void insertMusicTrackToPlaylistByUser(@AuthenticationPrincipal User user,
                                                 @RequestParam String titleOfPlaylist,
                                                 @RequestParam Integer trackId) {
        playlistService.insertMusicTrackToPlaylistByUser(user.getId(), titleOfPlaylist, trackId);
    }

    @GetMapping("/music")
    public List<Track> getMusicFromPlaylistByUser(@AuthenticationPrincipal User user, @RequestParam String titleOfPlaylist) {
        return playlistService.getMusicFromPlaylistByUser(user.getId(), titleOfPlaylist);
    }

    @DeleteMapping
    public void deletePlaylist(@AuthenticationPrincipal User user, @RequestParam Integer playlistId) {
        playlistService.deletePlaylist(user.getId(), playlistId);
    }

    @DeleteMapping("/music")
    public void deleteTrackPlaylist(@AuthenticationPrincipal User user, @RequestParam String playlistTitle, @RequestParam Integer trackId) {
        playlistService.deleteTrackFromPlaylist(user.getId(), playlistTitle, trackId);
    }
}