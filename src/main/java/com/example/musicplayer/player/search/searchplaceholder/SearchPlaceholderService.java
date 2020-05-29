package com.example.musicplayer.player.search.searchplaceholder;

import com.example.musicplayer.player.music.model.TrackDto;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SearchPlaceholderService {

    public String getRandomTrackToPutInSearchPlaceholder(Map<Integer, TrackDto> musicTracks) {
        Set<Integer> ids = musicTracks.values().stream().map(TrackDto::getId).collect(Collectors.toSet());
        int randomId = getRandomIdFromMap(ids, musicTracks.size());
        return musicTracks.values().stream().filter(e -> e.getId().equals(randomId)).map(TrackDto::getFullTitle).findFirst().orElse("");
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