package com.example.musicplayer.player.lyric.service;

import com.example.musicplayer.player.lyric.model.Lyric;
import com.example.musicplayer.player.music.service.MusicPlayerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LyricService {
    private final String lyricApiUrl;
    private final String lyricApiKey;
    private String oldNameOfTrack;
    private String bufferLyricText;
    private final Logger log = LoggerFactory.getLogger(LyricService.class.getName());

    public LyricService(@Value("${lyric-api-url}") String lyricApiUrl,
                        @Value("${lyric-apikey}") String lyricApiKey) {
        this.lyricApiUrl = lyricApiUrl;
        this.lyricApiKey = lyricApiKey;
    }

    public Lyric getLyric(String nameOfTrack, String artistOfTrack) {
        if (oldNameOfTrack == null || !oldNameOfTrack.equals(nameOfTrack)) {
            RestTemplate restTemplate = new RestTemplate();
            String plainJson = "";
            try {
                plainJson = restTemplate.getForObject(lyricApiUrl + artistOfTrack + "/" + nameOfTrack + "?apikey=" + lyricApiKey, String.class);
                if (plainJson != null && !plainJson.isBlank()) {
                    oldNameOfTrack = nameOfTrack;
                    bufferLyricText = parseApiJson(plainJson);
                    return new Lyric(parseApiJson(plainJson), true);
                }
            } catch (Exception e) {
                log.error(e.toString());
                return new Lyric(plainJson, false);
            }
        }
        return new Lyric(bufferLyricText, true);
    }

    private String parseApiJson(String json) {
        int begin = json.indexOf("text\":\"");
        int end = json.indexOf("\"lang");
        return json.substring(begin + 7, end - 2);
    }
}