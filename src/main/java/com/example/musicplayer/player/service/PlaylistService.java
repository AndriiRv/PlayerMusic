package com.example.musicplayer.player.service;

import com.example.musicplayer.player.model.Playlist;
import com.example.musicplayer.player.model.Track;
import com.example.musicplayer.player.repository.PlaylistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;
    private final MusicService musicService;

    @Autowired
    public PlaylistService(PlaylistRepository playlistRepository,
                           MusicService musicService) {
        this.playlistRepository = playlistRepository;
        this.musicService = musicService;
    }

    public void createPlaylist(int userId, String titleOfPlaylist) {
        playlistRepository.createPlaylist(userId, titleOfPlaylist);
    }

    public List<String> getAllPlaylistsByUser(int userId) {
        return playlistRepository.getAllPlaylistsByUser(userId);
    }

    public void insertMusicTrackToPlaylistByUser(int userId, String playlistTitle, String trackTitle) {
        Playlist playlist = playlistRepository.getPlaylistByUserIdTitle(userId, playlistTitle);
        Track track = musicService.getTrackByFullTitle(trackTitle);

        playlistRepository.insertMusicTrackToPlaylistByUser(playlist.getId(), track.getId());
    }

    public List<String> getMusicFromPlaylistByUser(int userId, String playlistTitle) {
        Playlist playlist = playlistRepository.getPlaylistByUserIdTitle(userId, playlistTitle);

        return playlistRepository.getMusicFromPlaylistByUser(userId, playlist.getId());
    }
}
