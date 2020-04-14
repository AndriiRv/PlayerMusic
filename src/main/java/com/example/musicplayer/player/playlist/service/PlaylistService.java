package com.example.musicplayer.player.playlist.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.player.playlist.model.Playlist;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.playlist.repository.PlaylistRepository;
import com.example.musicplayer.player.music.service.MusicService;
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

    public boolean createPlaylist(int userId, String titleOfPlaylist) {
        if (!titleOfPlaylist.isBlank()) {
            playlistRepository.createPlaylist(userId, titleOfPlaylist);
            return true;
        }
        return false;
    }

    public List<Playlist> getAllPlaylistsByUser(int userId) {
        return playlistRepository.getAllPlaylistsByUser(userId);
    }

    public void insertMusicTrackToPlaylistByUser(int userId, String playlistTitle, String trackTitle) {
        Playlist playlist = playlistRepository.getPlaylistByUserIdTitle(userId, playlistTitle);
        Track track = musicService.getTrackByFullTitle(trackTitle);

        playlistRepository.insertMusicTrackToPlaylistByUser(playlist.getId(), track.getId());
    }

    public List<Track> getMusicFromPlaylistByUser(int userId, String playlistTitle) {
        Playlist playlist = playlistRepository.getPlaylistByUserIdTitle(userId, playlistTitle);

        return playlistRepository.getMusicFromPlaylistByUser(userId, playlist.getId());
    }

    public void deletePlaylist(int userId, Integer playlistId) {
        playlistRepository.deletePlaylist(userId, playlistId);
    }

    public void deleteTrackFromPlaylist(int userId, String playlistTitle, int trackId) {
        Playlist playlist = playlistRepository.getPlaylistByUserIdTitle(userId, playlistTitle);
        playlistRepository.deleteTrackFromPlaylist(playlist.getId(), trackId);
    }
}
