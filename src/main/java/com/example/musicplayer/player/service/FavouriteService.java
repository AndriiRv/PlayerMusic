package com.example.musicplayer.player.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.service.UserService;
import com.example.musicplayer.player.model.Track;
import com.example.musicplayer.player.repository.FavouriteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final MusicService musicService;
    private final UserService userService;
    private final Logger log = LoggerFactory.getLogger(FavouriteService.class.getName());

    @Autowired
    public FavouriteService(FavouriteRepository favouriteRepository,
                            MusicService musicService,
                            UserService userService) {
        this.musicService = musicService;
        this.favouriteRepository = favouriteRepository;
        this.userService = userService;
    }

    public void setMusicToFavourite(int userId, String trackFullTitle) {
        Track track = musicService.getTrackByFullTitle(trackFullTitle);
        if (favouriteRepository.isTrackAlreadyInFavouriteByUserId(userId, track.getId()) == 0) {
            favouriteRepository.setMusicToFavouriteByUserId(userId, track.getId());
        }
        User user = userService.getUserByUserId(userId);
        log.info("{} added {} to favourite", user.getUsername(), trackFullTitle);
    }

    public List<String> getFavouriteTracks(int userId) {
        return favouriteRepository.getFavouriteTracksByUserId(userId);
    }

    public void deleteTrackFromFavourite(int userId, String trackFullTitle) {
        favouriteRepository.deleteTrackFromFavourite(userId, musicService.getTrackByFullTitle(trackFullTitle).getId());
    }
}
