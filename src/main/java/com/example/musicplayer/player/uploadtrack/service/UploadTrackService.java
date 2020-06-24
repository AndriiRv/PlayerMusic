package com.example.musicplayer.player.uploadtrack.service;

import com.example.musicplayer.player.music.model.Track;
import com.example.musicplayer.player.music.model.TrackList;
import com.example.musicplayer.player.music.service.MusicPlayerService;
import com.example.musicplayer.player.music.service.MusicService;
import com.example.musicplayer.player.music.service.MusicTrackSaverService;
import com.example.musicplayer.player.uploadtrack.repository.UploadTrackRepository;
import com.example.musicplayer.sign.user.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static com.example.musicplayer.config.ExceptionOutput.exceptionStacktraceToString;

@Service
public class UploadTrackService {
    private final UploadTrackRepository uploadTrackRepository;
    private final MusicService musicService;
    private final MusicTrackSaverService saverService;
    private final MusicPlayerService musicPlayerService;
    private final TrackList trackList;
    private final String pathToFolder;
    private final Logger log = LoggerFactory.getLogger(UploadTrackService.class.getName());

    public UploadTrackService(UploadTrackRepository uploadTrackRepository,
                              @Value("${music-player.directory}") String pathToFolder,
                              MusicService musicService,
                              MusicTrackSaverService saverService,
                              MusicPlayerService musicPlayerService,
                              TrackList trackList) {
        this.uploadTrackRepository = uploadTrackRepository;
        this.pathToFolder = pathToFolder;
        this.musicService = musicService;
        this.saverService = saverService;
        this.musicPlayerService = musicPlayerService;
        this.trackList = trackList;
    }

    public Set<Track> getUploadTracksByUserId(int userId) {
        return new LinkedHashSet<>(uploadTrackRepository.getUploadTracksByUserId(userId));
    }

    public boolean uploadTrack(User user, MultipartFile multipartFile) {
        if (!Objects.equals(multipartFile.getOriginalFilename(), "")) {
            String titleOfTrack = multipartFile.getOriginalFilename();
            if (titleOfTrack != null && titleOfTrack.endsWith("mp3") && (musicService.checkIfTrackExistInTable(titleOfTrack) == 0)) {
                try {
                    Files.write(Paths.get(pathToFolder + titleOfTrack), multipartFile.getBytes());
                    log.info("{} - was upload in db by: '{}'", titleOfTrack, user.getUsername());
                } catch (IOException e) {
                    log.warn(exceptionStacktraceToString(e));
                }
                musicService.checkIfTrackExistInSystem(pathToFolder, trackList.getMusicTracks());
                if (musicService.checkIfTrackExistInTable(titleOfTrack) == 0) {
                    Map<Integer, Track> integerTrackDtoMap = saverService.addTrackToDb();
                    uploadTrackRepository.uploadTrackByUserId(user.getId(), integerTrackDtoMap.keySet().stream().findFirst().orElse(0));
                    musicPlayerService.postConstruct();
                    return true;
                }
            }
        }
        return false;
    }
}