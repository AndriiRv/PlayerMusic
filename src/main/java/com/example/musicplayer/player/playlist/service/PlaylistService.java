package com.example.musicplayer.player.playlist.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.playlist.model.Playlist;
import com.example.musicplayer.player.playlist.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlaylistService {
    private final PlaylistRepository playlistRepository;

    public PlaylistService(PlaylistRepository playlistRepository) {
        this.playlistRepository = playlistRepository;
    }

    public void createPlaylist(int userId, String titleOfPlaylist) {
        if (!titleOfPlaylist.isBlank()) {
            playlistRepository.createPlaylist(userId, titleOfPlaylist);
        }
    }

    public List<Playlist> getAllPlaylistsByUser(int userId) {
        return playlistRepository.getAllPlaylistsByUser(userId);
    }

    public void insertMusicTrackToPlaylistByUser(int userId, String playlistTitle, int trackId) {
        if (!playlistTitle.isBlank()) {
            Playlist playlist = playlistRepository.getPlaylistByUserIdTitle(userId, playlistTitle);

            boolean isTrackAlreadyAddedToPlaylist = getMusicFromPlaylistByUser(userId, playlistTitle)
                    .stream().anyMatch(e -> e.getId().equals(trackId));

            if (!isTrackAlreadyAddedToPlaylist) {
                playlistRepository.insertMusicTrackToPlaylistByUser(playlist.getId(), trackId);
            }
        }
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
