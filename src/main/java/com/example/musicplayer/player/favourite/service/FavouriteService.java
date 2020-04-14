package com.example.musicplayer.player.favourite.service;

import com.example.musicplayer.authentication.model.User;
import com.example.musicplayer.authentication.service.UserService;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.favourite.repository.FavouriteRepository;
import com.example.musicplayer.player.music.service.MusicService;
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

    public boolean setMusicToFavourite(int userId, String trackFullTitle) {
        boolean checkIfTrackAlreadyInFavourite;

        Track track = musicService.getTrackByFullTitle(trackFullTitle);
        if (favouriteRepository.isTrackAlreadyInFavouriteByUserId(userId, track.getId()) == 0) {
            favouriteRepository.setMusicToFavouriteByUserId(userId, track.getId());
            checkIfTrackAlreadyInFavourite = false;
            log.info("{} added {} to favourite",
                    userService.getUserByUserId(userId).getUsername(),
                    trackFullTitle);
        } else {
            checkIfTrackAlreadyInFavourite = true;
        }
        return checkIfTrackAlreadyInFavourite;
    }

    public Integer getCountOfFavouriteByMusicId(int musicId) {
        return favouriteRepository.getCountOfFavouriteByMusicId(musicId);
    }

    public void renameTrackByUser(User user, int trackId, String newTitleByUser) {
        favouriteRepository.renameTrackByUser(user.getId(), trackId, newTitleByUser);
    }

    public Integer isTrackAlreadyInFavouriteByUserId(User user, int musicId) {
        return favouriteRepository.isTrackAlreadyInFavouriteByUserId(user.getId(), musicId);
    }

    public List<Track> getFavouriteTracksByUser(int userId) {
        return favouriteRepository.getFavouriteTracksByUserId(userId);
    }

    public void deleteTrackFromFavourite(int userId, String trackFullTitle) {
        favouriteRepository.deleteTrackFromFavourite(userId, musicService.getTrackByFullTitle(trackFullTitle).getId());
        log.info("{} remove {} from favourite", userService.getUserByUserId(userId).getUsername(), trackFullTitle);
    }
}
