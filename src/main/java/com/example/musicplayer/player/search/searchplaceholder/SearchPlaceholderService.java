package com.example.musicplayer.player.search.searchplaceholder;

import com.example.musicplayer.player.music.model.Track;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchPlaceholderService {

    public String getRandomTrackToPutInSearchPlaceholder(Set<Track> musicTracks) {
        Set<Integer> ids = musicTracks.stream().map(Track::getId).collect(Collectors.toSet());
        int randomId = getRandomIdFromMap(ids, musicTracks.size());
        return musicTracks.stream().filter(e -> e.getId().equals(randomId)).map(Track::getFullTitle).findFirst().orElse("");
    }

    private Integer getRandomIdFromMap(Set<Integer> ids, int musicTracksSize) {
        Integer randomId = null;
        while (randomId == null) {
            randomId = ids.stream()
                    .filter(e -> e.equals(new Random().nextInt(musicTracksSize)))
                    .findFirst()
                    .orElse(null);
        }
        return randomId;
    }
}