package com.example.musicplayer.player.lyric.service;

import com.example.musicplayer.player.lyric.model.Lyric;
import com.example.musicplayer.player.lyric.repository.LyricRepository;
import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.service.MusicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
public class LyricService {
    private final LyricRepository lyricRepository;
    private final MusicService musicService;
    private final String lyricApiUrl;
    private final String lyricApiKey;
    private String lyricText;
    private final Logger log = LoggerFactory.getLogger(LyricService.class.getName());

    public LyricService(LyricRepository lyricRepository,
                        MusicService musicService,
                        @Value("${lyric-api-url}") String lyricApiUrl,
                        @Value("${lyric-apikey}") String lyricApiKey) {
        this.lyricRepository = lyricRepository;
        this.musicService = musicService;
        this.lyricApiUrl = lyricApiUrl;
        this.lyricApiKey = lyricApiKey;
    }

    public Lyric getLyric(String nameOfTrack, String artistOfTrack) {
        Track track = musicService.getTrackByFullTitle(artistOfTrack + " - " + nameOfTrack + ".mp3");
        String lyricByMusicId = lyricRepository.getLyricByMusicId(track.getId());
        if (!lyricByMusicId.equals("")) {
            log.info("get lyric to {}, from db", track.getFullTitle());
            return new Lyric(lyricByMusicId, true);
        } else {
            RestTemplate restTemplate = new RestTemplate();
            String plainJson = "";
            try {
                plainJson = restTemplate.getForObject(lyricApiUrl + artistOfTrack + "/" + nameOfTrack + "?apikey=" + lyricApiKey, String.class);
                if (plainJson != null && !plainJson.isBlank()) {
                    lyricText = parseApiJson(plainJson);
                    lyricRepository.saveLyricToDb(null, track.getId(), lyricText);
                    log.info("get lyric to {}, from api", track.getFullTitle());
                    return new Lyric(lyricText, true);
                }
            } catch (Exception e) {
                log.error(Arrays.toString(e.getStackTrace()));
                return new Lyric(plainJson, false);
            }
        }
        return new Lyric(lyricText, true);
    }

    private String parseApiJson(String json) {
        int begin = json.indexOf("text\":\"");
        int end = json.indexOf("\"lang");
        return json.substring(begin + 7, end - 2).replace("\\n", "<br/>");
    }
}