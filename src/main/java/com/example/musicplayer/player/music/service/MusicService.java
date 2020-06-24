package com.example.musicplayer.player.music.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.repository.MusicRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MusicService {
    private final MusicRepository musicRepository;
    private final Logger log = LoggerFactory.getLogger(MusicService.class.getName());

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

    int insertMusicToDb(Track track) {
        return musicRepository.insertMusicToDb(track);
    }

    public Set<Track> getMusicFromTable() {
        Set<Track> musicTracks = new LinkedHashSet<>(musicRepository.getTracks());
        log.info("Load from bd");
        return musicTracks;
    }

    public void checkIfTrackExistInSystem(String pathToFolder, Set<Track> musicTracks) {
        File file = new File(pathToFolder);
        File[] tracks = file.listFiles();

        if (tracks != null && file.exists()) {
            Set<String> allMusicTitle = Arrays.stream(tracks)
                    .map(File::getName)
                    .filter(nameOfFile -> nameOfFile.endsWith(".mp3"))
                    .collect(Collectors.toSet());

            Set<String> musicTitleFromTable = musicTracks.stream()
                    .map(Track::getFullTitle)
                    .collect(Collectors.toSet());

            musicTitleFromTable.removeAll(allMusicTitle);

            musicTitleFromTable.forEach(titleOfTrack -> {
                removeMusicTrackFromDbByFullTitle(titleOfTrack);
                log.info("'{}' - removed from db", titleOfTrack);
            });
        }
    }

    private void removeMusicTrackFromDbByFullTitle(String fullTitle) {
        musicRepository.removeMusicTrackByFullTitle(fullTitle);
    }
}