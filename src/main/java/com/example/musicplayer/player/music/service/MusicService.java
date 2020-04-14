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

    public int insertMusicToDb(TrackDto trackDto) {
        return musicRepository.insertMusicToDb(converterFromDto(trackDto));
    }

    public Integer isMusicTableEmpty() {
        return musicRepository.isMusicTableEmpty();
    }

    public List<TrackDto> getMusicFromTable() {
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

    private Track converterFromDto(TrackDto trackDto) {
        Track track = new Track();
        track.setFullTitle(trackDto.getFullTitle());
        track.setSinger(trackDto.getSinger());
        track.setTitle(trackDto.getTitle());
        track.setSize(trackDto.getSize());
        track.setLength(trackDto.getLength());
        track.setDate(trackDto.getDate());
        track.setTime(trackDto.getTime());
        track.setDateTime(trackDto.getDateTime());
        track.setGenre(trackDto.getGenre());
        track.setYear(trackDto.getYear());
        track.setAlbumTitle(trackDto.getAlbumTitle());
        return track;
    }
}