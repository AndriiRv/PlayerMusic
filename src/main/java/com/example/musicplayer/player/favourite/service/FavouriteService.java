package com.example.musicplayer.player.favourite.service;

import com.example.musicplayer.player.favourite.repository.FavouriteRepository;
import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.music.service.MusicService;
import com.example.musicplayer.player.statistic.favourite.service.StatisticFavouriteService;
import com.example.musicplayer.sign.user.model.User;
import com.example.musicplayer.sign.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class FavouriteService {
    private final FavouriteRepository favouriteRepository;
    private final MusicService musicService;
    private final UserService userService;
    private final StatisticFavouriteService statisticFavouriteService;
    private final Logger log = LoggerFactory.getLogger(FavouriteService.class.getName());

    public FavouriteService(FavouriteRepository favouriteRepository,
                            MusicService musicService,
                            UserService userService,
                            StatisticFavouriteService statisticFavouriteService) {
        this.musicService = musicService;
        this.favouriteRepository = favouriteRepository;
        this.userService = userService;
        this.statisticFavouriteService = statisticFavouriteService;
    }

    public Integer getCountOfFavouriteMusicByUserId(int userId) {
        return favouriteRepository.getCountOfFavouriteMusicByUserId(userId);
    }

    public Map<String, Boolean> setMusicToFavourite(int userId, int trackId) {
        String fullTitle = musicService.getFullTitleByMusicId(trackId);
        Map<String, Boolean> map = new HashMap<>();

        if (favouriteRepository.isTrackAlreadyInFavouriteByUserId(userId, trackId) == 0) {
            favouriteRepository.setMusicToFavouriteByUserId(userId, trackId);

            statisticFavouriteService.addCountOfFavouriteByMusicId(trackId);

            map.put(fullTitle, false);
            log.info("{} added {} to favourite", userService.getUserByUserId(userId).getUsername(), fullTitle);
        } else {
            map.put(fullTitle, true);
        }
        return map;
    }

    public void renameTrackByUser(User user, int trackId, String newTitleByUser) {
        favouriteRepository.renameTrackByUser(user.getId(), trackId, newTitleByUser);
    }

    public Integer isTrackAlreadyInFavouriteByUserId(User user, int musicId) {
        return favouriteRepository.isTrackAlreadyInFavouriteByUserId(user.getId(), musicId);
    }

    public Set<TrackDto> getFavouriteTracksByUser(int userId) {
        return favouriteRepository.getFavouriteTracksByUserId(userId).stream()
                .map(TrackDto::of)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    public void deleteTrackFromFavourite(int userId, int trackId) {
        favouriteRepository.deleteTrackFromFavourite(userId, trackId);
        log.info("{} remove {} from favourite",
                userService.getUserByUserId(userId).getUsername(),
                musicService.getFullTitleByMusicId(trackId)
        );
    }
}
