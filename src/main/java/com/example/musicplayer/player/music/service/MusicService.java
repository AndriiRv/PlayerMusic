package com.example.musicplayer.player.music.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.model.TrackDto;
import com.example.musicplayer.player.music.repository.MusicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MusicService {
    private final MusicRepository musicRepository;

    @Autowired
    public MusicService(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    public Track getTrackByFullTitle(String trackFullTitle) {
        return musicRepository.getTrackByFullTitle(trackFullTitle);
    }

    public String getFullTitleByMusicId(int musicId) {
        return musicRepository.getFullTitleByMusicId(musicId);
    }

    public Integer checkIfTrackExistInTable(String fullTitle) {
        return musicRepository.checkIfTrackExistInTable(fullTitle);
    }

    public int insertMusicToDb(Track track) {
        return musicRepository.insertMusicToDb(track);
    }

    public Integer isMusicTableEmpty() {
        return musicRepository.isMusicTableEmpty();
    }

    public List<Track> getMusicFromTable() {
        return musicRepository.getMusicFromTable();
    }

    public List<String> getMusicTitleFromTable() {
        return musicRepository.getMusicTitleFromTable();
    }

    public void removeMusicTrackFromDbByFullTitle(String fullTitle) {
        musicRepository.removeMusicTrackFromDbByFullTitle(fullTitle);
    }

    public String getRandomTrackToPutInSearchPlaceholder() {
        if (isMusicTableEmpty() != 0) {
            return musicRepository.getRandomTrackToPutInSearchPlaceholder();
        }
        return "";
    }
}